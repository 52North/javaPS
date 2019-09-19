package org.n52.javaps.rest;

import io.swagger.model.Root;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface RootApi {

    String BASE_URL = "/rest";

    @ResponseBody
    @GetMapping(value = BASE_URL, produces = MediaTypes.APPLICATION_JSON)
    Root root();

}
