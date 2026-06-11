package org.clprolf.weatherapp.otherworkers.impl;

import org.clprolf.framework.ClWorker;
import org.clprolf.weatherapp.otheragents.impl.ClprolfWeatherApplicationImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

@ClWorker
public class ClprolfWeatherLauncherImpl {
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = SpringApplication.run(ClprolfWeatherApplicationImpl.class);
        ClprolfWeatherApplicationImpl.setApplicationContext(applicationContext);
    }
}
