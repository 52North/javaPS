package org.n52.javaps.annotation;

import org.junit.Test;
import org.n52.javaps.algorithm.annotation.BoundingBoxInput;

public class BoundingBoxAnnotationTest {

    @BoundingBoxInput(identifier= "bboxInput")
    
    @Test
    public void testBoundingBoxAnnotation() {
        
        BoundingBoxInput boundingBoxInput = getClass().getAnnotation(BoundingBoxInput.class);
        
//        new BoundingbIn
        
    }
    
}
