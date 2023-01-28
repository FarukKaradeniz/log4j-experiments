package com.farukkaradeniz.log4jexperiments;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;

@Builder
@Getter
public class ResponseLogModel {
    private String method;
    private OffsetDateTime date;
    private long elapsedTime;
    private Map<String, String> responseHeaders;
    private Object responseBody;
}
