package com.survivorsLabs.muscleCoach.common.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final LoggingService loggingService;
    private final HttpServletRequest httpServletRequest;

    /**
     * Handle request
     *
     * @param methodParameter method parameters
     * @param type            type
     * @param aClass          class
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * Handle request body of requests
     *
     * @param body          body
     * @param inputMessage  message
     * @param parameter     parameters
     * @param targetType    types
     * @param converterType convertType
     * @return object
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                                MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        try {
            loggingService.logRequest(httpServletRequest, body);
        } catch (JsonProcessingException e) {
            log.error("Error logging request: {}", e.getMessage());
        }

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}