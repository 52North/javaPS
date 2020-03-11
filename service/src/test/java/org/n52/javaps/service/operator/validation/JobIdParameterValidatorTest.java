/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service.operator.validation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import org.junit.Test;
import org.mockito.Mockito;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.impl.EngineImpl;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.exception.ResultNotReadyException;
import org.n52.shetland.ogc.wps.request.GetResultRequest;
import org.n52.shetland.ogc.wps.request.GetStatusRequest;

/**
 *
 * @author Matthes Rieke <m.rieke@52north.org>
 */
public class JobIdParameterValidatorTest {

    @Test
    public void testValidationGetStatus() throws OwsExceptionReport, JobNotFoundException {
        EngineImpl engine = Mockito.mock(EngineImpl.class);
        Mockito.when(engine.hasJob(Mockito.any())).thenReturn(true);
        Mockito.when(engine.getResult(Mockito.any())).thenReturn(new FutureTask<>(() -> {}, null));
        JobIdParameterValidator val = new JobIdParameterValidator();
        val.setEngine(engine);

        GetStatusRequest gsr = new GetStatusRequest("WPS", "2.0.0");
        gsr.setJobId("test-job");
        val.validate(gsr);
    }

    @Test(expected = ResultNotReadyException.class)
    public void testValidationGetResultNoResult() throws OwsExceptionReport, JobNotFoundException {
        EngineImpl engine = Mockito.mock(EngineImpl.class);
        Mockito.when(engine.hasJob(Mockito.any())).thenReturn(true);
        Mockito.when(engine.getResult(Mockito.any())).thenReturn(new FutureTask<>(() -> {}, null));
        JobIdParameterValidator val = new JobIdParameterValidator();
        val.setEngine(engine);

        GetResultRequest grr = new GetResultRequest("WPS", "2.0.0");
        grr.setJobId("test-job");
        val.validate(grr);
    }

    @Test
    public void testValidationGetResult() throws OwsExceptionReport, JobNotFoundException, InterruptedException, ExecutionException {
        EngineImpl engine = Mockito.mock(EngineImpl.class);
        Mockito.when(engine.hasJob(Mockito.any())).thenReturn(true);

        Result r = new Result();
        Future fr = Mockito.mock(Future.class);
        Mockito.when(fr.isDone()).thenReturn(true);
        Mockito.when(fr.get()).thenReturn(r);

        Mockito.when(engine.getResult(Mockito.any())).thenReturn(fr);
        JobIdParameterValidator val = new JobIdParameterValidator();
        val.setEngine(engine);

        GetResultRequest grr = new GetResultRequest("WPS", "2.0.0");
        grr.setJobId("test-job");
        val.validate(grr);
    }

}
