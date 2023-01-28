package com.farukkaradeniz.log4jexperiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public Logger.Level getLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Logger getLogger() {
        return new FeignLogger(objectMapper);
    }

}
