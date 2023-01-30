package com.farukkaradeniz.log4jexperiments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MyController {

    private final UserClient userClient;

    @GetMapping("/")
    ResponseEntity<Model> getUser(@RequestParam("test") String test) {
        log.info("Inside getUser " + test);
        return userClient.getUser();
    }


    @PostMapping("/")
    ResponseEntity<Model> test(@RequestBody Req req) {
        return ResponseEntity.ok()
                .header("testomer", "testval")
                .body(new Model("test", req.username(), "testemail"));
    }

    record Req(String username) {
    }

}
