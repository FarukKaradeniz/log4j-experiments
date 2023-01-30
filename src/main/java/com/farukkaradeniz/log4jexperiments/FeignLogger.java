package com.farukkaradeniz.log4jexperiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class FeignLogger extends Logger {

    private final ObjectMapper objectMapper;

    @Override
    protected void log(String s, String s1, Object... objects) {
    }

    @SneakyThrows
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        var requestLogBuilder = RequestLogModel.builder();
        var request = response.request();
        Reader reader = response.body().asReader(StandardCharsets.UTF_8);

        var responseString = IOUtils.toString(reader);
        requestLogBuilder.responseBody(objectMapper.readValue(responseString, Object.class));
        if (request.body() != null) {
            var requestBodyContent = new String(request.body());
            if (!requestBodyContent.isBlank()) {
                requestLogBuilder.requestBody(objectMapper.readValue(requestBodyContent, Object.class));
            }
        }
        requestLogBuilder
                .url(request.url())
                .direction("OUTBOUND")
                .httpStatus(response.status())
                .date(OffsetDateTime.now())
                .elapsedTime(elapsedTime)
                .method(response.request().httpMethod().name())
                .requestHeaders(headersToMap(request.headers()))
                .responseHeaders(headersToMap(response.headers()));
        log.info("Feign Request: " + objectMapper.writeValueAsString(requestLogBuilder.build()));

        return response.toBuilder()
                .body(responseString, StandardCharsets.UTF_8)
                .headers(response.headers()).build();
    }

    private Map<String, String> headersToMap(Map<String, Collection<String>> headers) {
        var map = new HashMap<String, String>();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            map.put(entry.getKey(), StringUtils.collectionToCommaDelimitedString(entry.getValue()));
        }
        return map;
    }

    private Map<String, String> headersToMap(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        var map = new HashMap<String, String>(headerNames.size());
        for (String headerName : headerNames) {
            String header = headerValueResolver.apply(headerName);
            map.put(headerName, header);
        }
        return map;
    }
}
