package com.farukkaradeniz.log4jexperiments;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;

@Builder
@Getter
public class RequestLogModel {
    private String url;
    private String direction;
    private String method;
    private int httpStatus;
    private OffsetDateTime date;
    private long elapsedTime;
    private Map<String, String> requestHeaders;
    private Object requestBody;
    private Map<String, String> responseHeaders;
    private Object responseBody;
}
