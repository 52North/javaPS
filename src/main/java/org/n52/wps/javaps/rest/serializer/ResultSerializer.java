package org.n52.wps.javaps.rest.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.n52.shetland.ogc.wps.data.ProcessData;

import io.swagger.model.OutputInfo;
import io.swagger.model.Result;
import io.swagger.model.ValueType;

public class ResultSerializer {

    public static Result serializeResult(org.n52.shetland.ogc.wps.Result result) throws IOException{
        
        Result serializedResult = new Result();
        
        List<OutputInfo> outputs = new ArrayList<>();
        
        Iterator<ProcessData> processResults = result.getOutputs().iterator();
        
        while (processResults.hasNext()) {
            ProcessData processData = (ProcessData) processResults.next();
            
            OutputInfo output = new OutputInfo();
            
            output.setId(processData.getId().getValue());
            
            ValueType valueType = new ValueType();
            
            if(processData.isReference()) {
                valueType.setHref(processData.asReference().getURI().toString());
            }else if(processData.isValue()) {
                StringWriter writer = new StringWriter();
                String encoding = StandardCharsets.UTF_8.name();
                try {
                    IOUtils.copy(processData.asValue().getData(), writer, encoding);
                } catch (IOException e) {
                    throw e;
                }
                valueType.setValue(writer.toString());
            }
            
            output.setValue(valueType);
            
            outputs.add(output);
        }
        
        serializedResult.setOutputs(outputs);
        
        return serializedResult;
        
    }
    
}
