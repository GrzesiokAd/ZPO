package com.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectRestApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProjectRestApiApplication.class);

    public static void main(String[] args) {
        logger.info("Uruchamianie aplikacji Project REST API");
        SpringApplication.run(ProjectRestApiApplication.class, args);
        logger.info("Aplikacja Project REST API została uruchomiona");
    }
}