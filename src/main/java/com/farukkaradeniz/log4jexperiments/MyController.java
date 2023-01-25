package com.farukkaradeniz.log4jexperiments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MyController {

    private final UserClient userClient;

    @GetMapping("/")
    ResponseEntity<Model> getUser() {
        log.info("Inside getUser");
        return userClient.getUser();
    }

    @GetMapping("/test")
    ResponseEntity<Model> test() {
        return ResponseEntity.ok(new Model("test", "testusername", "testemail"));
    }

}
