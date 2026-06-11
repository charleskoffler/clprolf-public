package org.clprolf.weatherapp.otheragents.impl;

import jakarta.annotation.PostConstruct;
import org.clprolf.framework.ClAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

/**
 *
 *
 */
@ClAgent
@SpringBootApplication(scanBasePackages = "org.clprolf.weatherapp")
@EnableJpaRepositories(basePackages = "org.clprolf.weatherapp.repos")
@EntityScan(basePackages = "org.clprolf.weatherapp.entities")
public class ClprolfWeatherApplicationImpl
{
    @PostConstruct
    public void init() {
        // Force la JVM à utiliser l'UTC pour toutes les opérations de date/heure
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    private static Logger logger = LoggerFactory.getLogger(ClprolfWeatherApplicationImpl.class);

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ClprolfWeatherApplicationImpl.applicationContext = applicationContext;
    }
}
