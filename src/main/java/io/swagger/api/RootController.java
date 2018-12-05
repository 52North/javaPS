package io.swagger.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.model.Link;
import io.swagger.model.Root;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
@Configurable
public class RootController {

    final String baseURL = "/rest";
    
    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", baseURL);
    }
    
    @RequestMapping(value = "/rest/")
    public ResponseEntity<Root> index() {
        
        List<Link> links = new ArrayList<>();
        
        Link link = new Link();
        
        link.setHref(serviceURL + "/api/");
        
        links.add(link);
        
        link = new Link();
        
        link.setHref(serviceURL + "/conformance/");
        
        links.add(link);
        
        link = new Link();
        
        link.setHref(serviceURL + "/processes/");
        
        links.add(link);
        
        Root root = new Root();
        
        root.setLinks(links);
        
        return ResponseEntity.ok(root);
    }
}
