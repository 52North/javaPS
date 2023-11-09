/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * A fully buffered output stream using local disk storage for large data.
 * <p>
 * Initially this output stream buffers to memory, like ByteArrayOutputStream
 * might do, but it shifts to using an on disk temporary file if the output gets
 * too large.
 * <p>
 * The content of this buffered stream may be sent to another OutputStream only
 * after this stream has been properly closed by {@link #close()}.
 */
public class LargeBufferStream extends OutputStream {
    private static final int DEFAULT_IN_CORE_LIMIT = 128 * 1024 * 1024;

    /** Chain of data, if we are still completely in-core; otherwise null. */
    private ArrayList<Block> blocks;

    /**
     * Maximum number of bytes we will permit storing in memory.
     * <p>
     * When this limit is reached the data will be shifted to a file on disk,
     * preventing the JVM heap from growing out of control.
     */
    private int inCoreLimit;

    /**
     * Location of our temporary file if we are on disk; otherwise null.
     * <p>
     * If we exceeded the {@link #inCoreLimit} we nulled out {@link #blocks} and
     * created this file instead. All output goes here through {@link #diskOut}.
     */
    private File onDiskFile;

    /** If writing to {@link #onDiskFile} this is a buffered stream to it. */
    private OutputStream diskOut;

    /** Create a new empty temporary buffer. */
    public LargeBufferStream() {
        inCoreLimit = DEFAULT_IN_CORE_LIMIT;
        blocks = new ArrayList<Block>(inCoreLimit / Block.SZ);
        blocks.add(new Block());
    }

    @Override
    public void write(final int b) throws IOException {
        if (blocks == null) {
            diskOut.write(b);
            return;
        }

        Block s = last();
        if (s.isFull()) {
            if (reachedInCoreLimit()) {
                diskOut.write(b);
                return;
            }

            s = new Block();
            blocks.add(s);
        }
        s.buffer[s.count++] = (byte) b;
    }

    @Override
    public void write(final byte[] b,
            int off,
            int len) throws IOException {

        int offCopy = off;
        int lenCopy = len;

        if (blocks != null) {
            while (lenCopy > 0) {
                Block s = last();
                if (s.isFull()) {
                    if (reachedInCoreLimit()) {
                        break;
                    }

                    s = new Block();
                    blocks.add(s);
                }

                final int n = Math.min(Block.SZ - s.count, lenCopy);
                System.arraycopy(b, offCopy, s.buffer, s.count, n);
                s.count += n;
                lenCopy -= n;
                offCopy += n;
            }
        }

        if (lenCopy > 0) {
            diskOut.write(b, offCopy, lenCopy);
        }
    }

    private Block last() {
        return blocks.get(blocks.size() - 1);
    }

    private boolean reachedInCoreLimit() throws IOException {
        if (blocks.size() * Block.SZ < inCoreLimit) {
            return false;
        }

        onDiskFile = Files.createTempFile("jgit_", ".buffer").toFile();
        diskOut = new FileOutputStream(onDiskFile);

        final Block last = blocks.remove(blocks.size() - 1);
        for (final Block b : blocks) {
            diskOut.write(b.buffer, 0, b.count);
        }
        blocks = null;

        diskOut = new BufferedOutputStream(diskOut, Block.SZ);
        diskOut.write(last.buffer, 0, last.count);
        return true;
    }

    @Override
    public void close() throws IOException {
        if (diskOut != null) {
            try {
                diskOut.close();
            } finally {
                diskOut = null;
            }
        }
    }

    /**
     * Obtain the length (in bytes) of the buffer.
     * <p>
     * The length is only accurate after {@link #close()} has been invoked.
     *
     * @return total length of the buffer, in bytes.
     */
    public long length() {
        if (onDiskFile != null) {
            return onDiskFile.length();
        }

        final Block last = last();
        return ((long) blocks.size()) * Block.SZ - (Block.SZ - last.count);
    }

    /**
     * Send this buffer to an output stream.
     * <p>
     * This method may only be invoked after {@link #close()} has completed
     * normally, to ensure all data is completely transferred.
     *
     * @param os
     *            stream to send this buffer's complete content to.
     * @throws IOException
     *             an error occurred reading from a temporary file on the local
     *             system, or writing to the output stream.
     */
    public void writeTo(final OutputStream os) throws IOException {

        if (blocks != null) {
            // Everything is in core so we can stream directly to the output.
            //
            for (final Block b : blocks) {
                os.write(b.buffer, 0, b.count);

            }
        } else {
            // Reopen the temporary file and copy the contents.
            //
            final FileInputStream in = new FileInputStream(onDiskFile);
            try {
                int cnt;
                final byte[] buf = new byte[Block.SZ];
                while ((cnt = in.read(buf)) >= 0) {
                    os.write(buf, 0, cnt);

                }
            } finally {
                in.close();
            }
        }
    }

    /** Clear this buffer so it has no data, and cannot be used again. */
    public void destroy() {
        blocks = null;

        if (diskOut != null) {
            try {
                diskOut.close();
            } catch (IOException err) {
                // We shouldn't encounter an error closing the file.
            } finally {
                diskOut = null;
            }
        }

        if (onDiskFile != null) {
            if (!onDiskFile.delete()) {
                onDiskFile.deleteOnExit();
            }
            onDiskFile = null;
        }
    }

    private static class Block {
        static final int SZ = 8 * 1024 * 1024;

        private final byte[] buffer = new byte[SZ];

        private int count;

        boolean isFull() {
            return count == SZ;
        }
    }
}
