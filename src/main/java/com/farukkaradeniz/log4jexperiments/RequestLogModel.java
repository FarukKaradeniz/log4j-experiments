package com.farukkaradeniz.log4jexperiments;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;

@Builder
@Getter
public class RequestLogModel {
    private String method;
    private OffsetDateTime date;
    private Map<String, String> requestHeaders;
    private Object requestBody;
}
