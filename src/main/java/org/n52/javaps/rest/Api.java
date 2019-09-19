package org.n52.javaps.rest;

import org.springframework.web.bind.annotation.GetMapping;

public interface Api {
    String BASE_URL = RootApi.BASE_URL + "/api";

    @GetMapping(BASE_URL)
    String api();
}
