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
import com.google.common.collect.Maps;
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

    private static Properties gitProps;

    private static Properties versionProps;

    private String serviceURL;

    @Setting(SERVICE_URL)
    public void setServiceURL(final URI serviceURL) throws ConfigurationError {
        Validation.notNull("Service URL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url;
    }

    public Endpoint() {
        URL gitPropUrl = Resources.getResource("git.properties");
        ByteSource gitSource = Resources.asByteSource(gitPropUrl);
        gitProps = new Properties();
        try (InputStream in = gitSource.openStream()) {
            log.info("Loading git properties from {} [via {}]", gitPropUrl, in);
            gitProps.load(in);
        } catch (IOException e) {
            log.error("Could not load git properties", e);
        }

        URL verPropUrl = Resources.getResource("version.properties");
        ByteSource verSource = Resources.asByteSource(verPropUrl);
        versionProps = new Properties();
        try (InputStream in = verSource.openStream()) {
            log.info("Loading version properties from {} [via {}]", gitPropUrl, in);
            versionProps.load(in);
        } catch (IOException e) {
            log.error("Could not load version properties", e);
        }

        log.info("NEW {}", this);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> info() {
        Map<String, Object> infoMap = Maps.newHashMap();

        infoMap.put("endpoint", serviceURL);
        try {
            infoMap.put("version",
                    ImmutableMap.of("branch", gitProps.get("git.branch"),
                            "commitid", gitProps.get("git.commit.id"),
                            "buildtime", gitProps.get("git.build.time")));
        } catch (RuntimeException e) {
            log.error("Error retrieving git information from {}", gitProps, e);
        }
        try {
            infoMap.put("build",
                    ImmutableMap.of("version", versionProps.get("build.version"),
                            "date", versionProps.get("build.date")));
        } catch (RuntimeException e) {
            log.error("Error retrieving version information from {}", versionProps, e);
        }

        return infoMap;
    }

}
