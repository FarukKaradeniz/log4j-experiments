package com.farukkaradeniz.log4jexperiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.list;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = requestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = responseWrapper(response);

        var start = OffsetDateTime.now();
        filterChain.doFilter(requestWrapper, responseWrapper);
        logRequest(requestWrapper);
        logResponse(responseWrapper, ChronoUnit.MILLIS.between(start, OffsetDateTime.now()));
        responseWrapper.copyBodyToResponse();
    }

    @SneakyThrows
    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        var logModelBuilder = RequestLogModel.builder();
        var requestBodyContent = new String(requestWrapper.getContentAsByteArray());
        if (!requestBodyContent.isBlank()) {
            logModelBuilder.requestBody(objectMapper.readValue(requestBodyContent, Object.class));
        }

        logModelBuilder
                .date(OffsetDateTime.now())
                .requestHeaders(headersToMap(list(requestWrapper.getHeaderNames()), requestWrapper::getHeader))
                .method(requestWrapper.getMethod());
        log.info("Request: " + objectMapper.writeValueAsString(logModelBuilder.build()));
    }

    @SneakyThrows
    private void logResponse(ContentCachingResponseWrapper responseWrapper, long elapsedTime) {
        var logModelBuilder = ResponseLogModel.builder();

        var responseBodyContent = new String(responseWrapper.getContentAsByteArray());
        if (!responseBodyContent.isBlank()) {
            logModelBuilder.responseBody(objectMapper.readValue(responseBodyContent, Object.class));
        }
        logModelBuilder
                .date(OffsetDateTime.now())
                .elapsedTime(elapsedTime)
                .responseHeaders(headersToMap(responseWrapper.getHeaderNames(), responseWrapper::getHeader));
        log.info("Response: " + objectMapper.writeValueAsString(logModelBuilder.build()));
    }


    private String headersToString(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        StringBuilder builder = new StringBuilder();
        for (String headerName : headerNames) {
            String header = headerValueResolver.apply(headerName);
            builder.append("%s=%s".formatted(headerName, header)).append("\n");
        }
        return builder.toString();
    }

    private Map<String, String> headersToMap(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        var map = new HashMap<String, String>(headerNames.size());
        for (String headerName : headerNames) {
            String header = headerValueResolver.apply(headerName);
            map.put(headerName, header);
        }
        return map;
    }

    private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }

}
