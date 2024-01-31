package com.survivorsLabs.muscleCoach.common.logging;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class LoggingWebMvcConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    public LoggingWebMvcConfig(LoggingInterceptor loggingInterceptorI) {
        this.loggingInterceptor = loggingInterceptorI;
    }

    /**
     * Add interceptors
     *
     * @param registry interceptors registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}