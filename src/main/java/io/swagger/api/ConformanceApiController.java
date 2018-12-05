package io.swagger.api;

import io.swagger.model.Exception;
import io.swagger.model.ReqClasses;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T09:36:12.450Z[GMT]")

@Controller
public class ConformanceApiController implements ConformanceApi {

    private static final Logger log = LoggerFactory.getLogger(ConformanceApiController.class);

//    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ConformanceApiController(HttpServletRequest request) {
//        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ReqClasses> getRequirementsClasses() {
        String accept = request.getHeader("Accept");
        
        ReqClasses reqClasses = new ReqClasses();
        
        List<String> conformsTo = new ArrayList<>();
        
        conformsTo.add("http://www.opengis.net/spec/wps-rest/req/core"); 
        conformsTo.add("http://www.opengis.net/spec/wps-rest/req/oas30");
        conformsTo.add("http://www.opengis.net/spec/wps-rest/req/html");
        
        reqClasses.setConformsTo(conformsTo);
        
        return ResponseEntity.ok(reqClasses);
    }

}
