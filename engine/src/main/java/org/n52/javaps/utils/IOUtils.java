/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
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

import static java.util.stream.Collectors.toList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;

public final class IOUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);
    private static final String FILE = "file";
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    private IOUtils() {
    }

    /**
     * Reads the given input stream as a string and decodes that base64 string
     * into a file with the specified extension
     *
     * @param input
     *            the stream with the base64 string
     * @param extension
     *            the extension of the result file (without the '.' at the
     *            beginning)
     *
     * @return the decoded base64 file written to disk
     *
     * @throws IOException
     *             if an error occurs while writing the contents to disk
     */
    public static File writeBase64ToFile(InputStream input,
            String extension) throws IOException {
        return writeBase64(extension, input).toFile();
    }

    public static File writeStreamToFile(InputStream inputStream,
            String extension) throws IOException {
        File file = File.createTempFile(FILE + UUID.randomUUID(), "." + extension);
        return writeStreamToFile(inputStream, extension, file);
    }

    /**
     * Copies the input stream to the specified file.
     *
     * @param inputStream
     *            the input stream
     * @param extension
     *            the file extension (ignored)
     * @param file
     *            the file
     *
     * @return the file
     *
     * @throws java.io.IOException
     *             if an error occurs
     * @deprecated use
     *             {@link Files#copy(java.io.InputStream, java.nio.file.Path, java.nio.file.CopyOption...) }
     */
    @Deprecated
    public static File writeStreamToFile(InputStream inputStream,
            String extension,
            File file) throws IOException {
        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file;
    }

    /**
     * Zip the files. Returns a zipped file and delete the specified files
     *
     * @param files
     *            files to zipped
     *
     * @return the zipped file
     *
     * @throws IOException
     *             if the zipping process fails.
     */
    public static File zip(File... files) throws IOException {
        File zip = File.createTempFile("zip" + UUID.randomUUID(), ".zip");

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip))) {
            byte[] buffer = new byte[4096];
            for (File file : files) {
                if (!file.exists()) {
                    LOGGER.debug("Could not zip " + file.getAbsolutePath());
                    continue;
                }

                out.putNextEntry(new ZipEntry(file.getName()));
                try (FileInputStream in = new FileInputStream(file)) {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }

                    out.closeEntry();
                }
            }

            deleteResources(files);
        }

        return zip;
    }

    /**
     * Unzip the file. Returns the unzipped file with the specified extension
     * and deletes the zipped file
     *
     * @param file
     *            the file to unzip
     * @param extension
     *            the extension to search in the content files
     *
     * @return the file with the specified extension
     *
     * @throws IOException
     *             if the unzipping process fails
     */
    public static List<File> unzip(File file,
            String extension) throws IOException {
        return unzip(file, extension, null);
    }

    public static List<File> unzip(File file,
            String extension,
            File directory) throws IOException {
        return unzipAll(file).stream().filter(f -> f.getName().endsWith("." + extension)).collect(toList());
    }

    public static List<File> unzipAll(File file) throws IOException {

        byte[] buffer = new byte[2048];
        List<File> foundFiles = new ArrayList<>();

        File tempDir = Files.createTempDirectory("unzipped").toFile();

        ZipEntry entry;
        int count;
        File entryFile;

        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.isEmpty()) {
                    throw new IOException("Empty zip entry.");
                }
                entryFile = new File(tempDir, entryName);
                if (!entryFile.toPath().normalize().startsWith(tempDir.toPath())) {
                    throw new IOException("Bad zip entry: " + entryName);
                }
                boolean created = entryFile.createNewFile();
                if (!created) {
                    LOGGER.info("File already exists: " + entryFile.getAbsolutePath());
                    continue;
                }
                try (BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(entryFile),
                        buffer.length)) {
                    while ((count = zipInputStream.read(buffer)) != -1) {
                        dest.write(buffer, 0, count);
                    }
                    dest.flush();
                }
                foundFiles.add(entryFile);
            }
        }

        deleteResources(file);

        return foundFiles;
    }

    /**
     * Delete the given files and all the files with the same name but different
     * extension. If some file is <code>null</code> just doesn't process it and
     * continue to the next element of the array
     *
     * @param files
     *            the files to delete
     */
    public static void deleteResources(File... files) {
        for (File file : files) {
            if (file != null) {
                if (file.getAbsolutePath().startsWith(TMP_DIR)) {
                    delete(file);
                    File parent = file.getAbsoluteFile().getParentFile();
                    if (parent != null && !(parent.getAbsolutePath().equals(TMP_DIR))) {
                        parent.deleteOnExit();
                    }
                }
            }
        }
    }

    /**
     * Delete the given files and all the files with the same name but different
     * extension. If some file is <code>null</code> just doesn't process it and
     * continue to the next element of the array
     *
     * @param files
     *            the files to delete
     */
    private static void delete(File... files) {
        for (File file : files) {
            if (file != null) {
                final String baseName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                File[] list = file.getAbsoluteFile().getParentFile().listFiles(pathname -> pathname.getName()
                        .startsWith(baseName));
                if (list != null) {
                    for (File f : list) {
                        f.deleteOnExit();
                    }
                }
                file.deleteOnExit();
            }
        }
    }

    public static Path writeBase64(String extension,
            InputStream input) throws IOException {
        Path file = Files.createTempFile(FILE, ".".concat(extension));
        try (InputStream in = new Base64InputStream(new BufferedInputStream(input), true);
                OutputStream out = new BufferedOutputStream(Files.newOutputStream(file))) {
            ByteStreams.copy(in, out);
        }
        return file;
    }
}
