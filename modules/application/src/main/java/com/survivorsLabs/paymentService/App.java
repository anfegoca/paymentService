package com.survivorsLabs.muscleCoach;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class App implements ApplicationListener<ContextRefreshedEvent> {

    private final Environment environment;

    public static final String LONG_LINE = "------------------------------------------------";
    public static final String LOG_START_PROJECT = "Aplicación iniciada: [{}]";
    public static final String LOG_PORT_OF_PROJECT = "Puerto: [{}]";
    public static final String LOG_PROJECT_VERSION = "Versión: [{}]";
    public static final String LOG_CONTEXT_PATH = "Context path: [{}]";

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info(LONG_LINE);
        log.info(LOG_START_PROJECT, environment.getProperty("component.name", ""));
        log.info(LOG_PROJECT_VERSION, environment.getProperty("component.version", ""));
        log.info(LOG_PORT_OF_PROJECT, environment.getProperty("server.port", ""));
        log.info(LOG_CONTEXT_PATH, environment.getProperty("server.servlet.context-path", ""));
        log.info(LONG_LINE);
    }
}
