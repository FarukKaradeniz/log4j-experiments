package com.farukkaradeniz.log4jexperiments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Log4jExperimentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Log4jExperimentsApplication.class, args);
    }

}
