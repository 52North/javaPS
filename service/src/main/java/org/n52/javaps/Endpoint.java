package org.n52.javaps;

import static org.n52.iceland.service.ServiceSettings.SERVICE_URL;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.util.Validation;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
@Controller
@RequestMapping(value = "/service", produces = MediaType.APPLICATION_JSON_VALUE)
@Configurable
public class Endpoint {
    private static final Logger log = LoggerFactory.getLogger(Endpoint.class);
    private static final Properties GIT_PROPS;
    private static final Properties VERSION_PROPS;

    static {
        URL gitPropUrl = Resources.getResource("git.properties");
        ByteSource gitSource = Resources.asByteSource(gitPropUrl);
        GIT_PROPS = new Properties();
        try (InputStream in = gitSource.openStream()) {
            log.info("Loading git properties from {} [via {}]", gitPropUrl, in);
            GIT_PROPS.load(in);
        } catch (IOException e) {
            throw new Error("Could not load git properties", e);
        }

        URL verPropUrl = Resources.getResource("version.properties");
        ByteSource verSource = Resources.asByteSource(verPropUrl);
        VERSION_PROPS = new Properties();
        try (InputStream in = verSource.openStream()) {
            log.info("Loading version properties from {} [via {}]", gitPropUrl, in);
            VERSION_PROPS.load(in);
        } catch (IOException e) {
            throw new Error("Could not load version properties", e);
        }
    }

    private String serviceURL;

    public Endpoint() {
        log.info("Created endpoint");
    }

    @Setting(SERVICE_URL)
    public void setServiceURL(final URI serviceURL) throws ConfigurationError {
        Validation.notNull("Service URL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> info() {
        return ImmutableMap.<String, Object>builder()
                .put("endpoint", this.serviceURL)
                .put("version", ImmutableMap.builder()
                     .put("branch", GIT_PROPS.get("git.branch"))
                     .put("commitid", GIT_PROPS.get("git.commit.id"))
                     .put("buildtime", GIT_PROPS.get("git.build.time")).build())
                .put("build", ImmutableMap.builder()
                     .put("version", VERSION_PROPS.get("build.version"))
                     .put("date", VERSION_PROPS.get("build.date")).build())
                .build();
    }

}
