package com.farukkaradeniz.log4jexperiments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MyController {

    private final UserClient userClient;

    @GetMapping("/")
    Model getUser() {
        log.info("Inside getUser");
        return userClient.getUser();
    }

}
