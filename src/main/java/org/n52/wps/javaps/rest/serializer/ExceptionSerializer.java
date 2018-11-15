package org.n52.wps.javaps.rest.serializer;

import io.swagger.model.Exception;

public class ExceptionSerializer {

    public static Exception serializeException(String code, String description) {
        
        Exception exception = new Exception();
        
        exception.setCode(code);
        
        exception.setDescription(description);
        
        return exception;
    }
    
}
