package io.swagger.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiController {

    @RequestMapping(value = "/rest/api")
    public String index() {
        
        return "redirect:https://app.swaggerhub.com/apis/geoprocessing/WPS/0.01";
    }
    
}
