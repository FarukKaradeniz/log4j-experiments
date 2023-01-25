package com.farukkaradeniz.log4jexperiments;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@Builder
@Getter
public class LogModel {
    private String method;
    private Date date;
    private Map<String, String> requestHeaders;
    private Object requestBody;
    private Map<String, String> responseHeaders;
    private Object responseBody;
}
