package com.phonebook.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    final static Logger log = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    private volatile boolean loggedMissingBuffering;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(request, response);
        return response;
    }

    protected void logRequest(HttpRequest request, byte[] body) {
        StringBuilder builder = new StringBuilder("Sending ").append(request.getMethod()).append(" request to ").append(request.getURI());
        if (body.length > 0 && hasTextBody(request.getHeaders())) {
            String bodyText = new String(body, determineCharset(request.getHeaders()));
            builder.append(": [").append(bodyText).append("]");
        }
        log.debug(builder.toString());
    }

    protected void logResponse(HttpRequest request, ClientHttpResponse response) {
        if (log.isDebugEnabled()) {
            try {
                StringBuilder builder = new StringBuilder("Received \"")
                        .append(response.getRawStatusCode()).append(" ").append(response.getStatusText()).append("\" response for ")
                        .append(request.getMethod()).append(" request to ").append(request.getURI());
                HttpHeaders responseHeaders = response.getHeaders();
                long contentLength = responseHeaders.getContentLength();
                if (contentLength != 0) {
                    if (hasTextBody(responseHeaders) && isBuffered(response)) {
                        String bodyText = StreamUtils.copyToString(response.getBody(), determineCharset(responseHeaders));
                        builder.append(": [").append(bodyText).append("]");
                    } else {
                        if (contentLength == -1) {
                            builder.append(" with content of unknown length");
                        } else {
                            builder.append(" with content of length ").append(contentLength);
                        }
                        MediaType contentType = responseHeaders.getContentType();
                        if (contentType != null) {
                            builder.append(" and content type ").append(contentType);
                        } else {
                            builder.append(" and unknown context type");
                        }
                    }
                }
                log.debug(builder.toString());
            } catch (IOException e) {
                log.warn("Failed to log response for {} request to {}", request.getMethod(), request.getURI(), e);
            }
        }
    }

    protected boolean hasTextBody(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            String subtype = contentType.getSubtype();
            return "text".equals(contentType.getType()) || "xml".equals(subtype) || "json".equals(subtype);
        }
        return false;
    }

    protected Charset determineCharset(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            try {
                Charset charSet = contentType.getCharset();
                if (charSet != null) {
                    return charSet;
                }
            } catch (UnsupportedCharsetException e) {
                // ignore
            }
        }
        return StandardCharsets.UTF_8;
    }

    protected boolean isBuffered(ClientHttpResponse response) {
        // class is non-public, so we check by name
        boolean buffered = "org.springframework.http.client.BufferingClientHttpResponseWrapper".equals(response.getClass().getName());
        if (!buffered && !loggedMissingBuffering) {
            log.warn("Can't log HTTP response bodies, as you haven't configured the RestTemplate with a BufferingClientHttpRequestFactory");
            loggedMissingBuffering = true;
        }
        return buffered;
    }
}