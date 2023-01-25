package com.farukkaradeniz.log4jexperiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
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

        filterChain.doFilter(requestWrapper, responseWrapper);

        var logModelBuilder = LogModel.builder();
        var requestBodyContent = new String(requestWrapper.getContentAsByteArray());
        if (!requestBodyContent.isBlank()) {
            logModelBuilder.requestBody(objectMapper.readValue(requestBodyContent, Object.class));
        }
        var responseBodyContent = new String(responseWrapper.getContentAsByteArray());
        if (!responseBodyContent.isBlank()) {
            logModelBuilder.responseBody(objectMapper.readValue(responseBodyContent, Object.class));
        }
        logModelBuilder
                .date(new Date())
                .requestHeaders(headersToMap(list(request.getHeaderNames()), request::getHeader))
                .method(request.getMethod())
                .responseHeaders(headersToMap(response.getHeaderNames(), response::getHeader));
        responseWrapper.copyBodyToResponse();
        log.info(objectMapper.writeValueAsString(logModelBuilder.build()));
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
