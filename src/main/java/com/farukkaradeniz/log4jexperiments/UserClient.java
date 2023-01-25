package com.farukkaradeniz.log4jexperiments;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "RandomUserApi", url = "https://random-data-api.com/api/v2")
public interface UserClient {
    @GetMapping(value = "/users")
    ResponseEntity<Model> getUser();
}
