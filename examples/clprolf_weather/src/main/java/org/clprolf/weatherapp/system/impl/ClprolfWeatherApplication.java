package org.clprolf.weatherapp.system.impl;

import jakarta.annotation.PostConstruct;
import org.clprolf.framework.ClSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@ClSystem
@SpringBootApplication(scanBasePackages = "org.clprolf.weatherapp")
@EnableJpaRepositories(basePackages = "org.clprolf.weatherapp.repos")
@EntityScan(basePackages = "org.clprolf.weatherapp.entities")
public class ClprolfWeatherApplication {

    @PostConstruct
    public void init() {
        // // Force the JVM to use UTC for all date/time operations
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    private static Logger logger = LoggerFactory.getLogger(ClprolfWeatherApplication.class);

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ClprolfWeatherApplication.applicationContext = applicationContext;
    }

    static void main( String[] args )
    {
        ApplicationContext applicationContext = SpringApplication.run(ClprolfWeatherApplication.class);
        ClprolfWeatherApplication.setApplicationContext(applicationContext);
    }
}
