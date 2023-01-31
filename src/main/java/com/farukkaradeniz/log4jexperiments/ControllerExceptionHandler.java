package com.farukkaradeniz.log4jexperiments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException ex, WebRequest request) {
        log.error("CustomException Handler: " + getStackTraceAsString(ex));

        return new ResponseEntity<>(Map.of("Exception Message", ex.getMessage()), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> genericException(Throwable thr) {
        log.error("Generic Exception Handler: " + getStackTraceAsString(thr));

        return new ResponseEntity<>("GENERIC EXCEPTION", HttpStatus.BAD_REQUEST);
    }


    private String getStackTraceAsString(Throwable throwable) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return "Unable to retrieve stack trace.";
        }
    }
}
