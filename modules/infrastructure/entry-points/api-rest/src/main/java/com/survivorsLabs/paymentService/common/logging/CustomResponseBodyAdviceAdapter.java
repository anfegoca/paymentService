package com.survivorsLabs.muscleCoach.common.logging;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final LoggingService loggingService;

    /**
     * Response handler
     *
     * @param methodParameter method parameter
     * @param aClass          class
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * Handle response body
     *
     * @param object             object
     * @param methodParameter    method parameter
     * @param mediaType          media type
     * @param aClass             class
     * @param serverHttpRequest  request
     * @param serverHttpResponse response
     * @return object
     */
    @Override
    public Object beforeBodyWrite(Object object,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (serverHttpRequest instanceof ServletServerHttpRequest &&
                serverHttpResponse instanceof ServletServerHttpResponse) {
            try {
                HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
                loggingService.logResponse(response, object);
            } catch (JsonProcessingException e) {
                log.error("Error logging response: {}", e.getMessage());
            }
        }
        return object;
    }
}