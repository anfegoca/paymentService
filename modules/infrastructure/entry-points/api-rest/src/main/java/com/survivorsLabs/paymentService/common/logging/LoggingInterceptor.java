package com.survivorsLabs.muscleCoach.common.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.lang.annotation.Annotation;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements AsyncHandlerInterceptor {

    private final LoggingService loggingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {

        boolean hasRequestBody = false;
        if (handler instanceof HandlerMethod) {
            hasRequestBody = notHasRequestBody((HandlerMethod) handler);
        }

        if (isGetRequest(request) || hasRequestBody) {
            try {
                loggingService.logRequest(request, "{}");
            } catch (JsonProcessingException e) {
                log.error("Error logging request: {}", e.getMessage());
            }
        }

        return true;
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && request.getMethod().equals(HttpMethod.GET.name());
    }

    private boolean notHasRequestBody(HandlerMethod handler) {
        for (MethodParameter parameter : handler.getMethodParameters()) {
            Annotation annotation = parameter.getParameterAnnotation(RequestBody.class);
            if (annotation != null) {
                return false;
            }
        }
        return true;
    }
}