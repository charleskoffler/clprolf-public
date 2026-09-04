package org.clprolf.weatherapp.agents.services.impl;

import org.clprolf.framework.ClAgent;
import org.clprolf.weatherapp.agents.services.WeatherAgent;
import org.clprolf.weatherapp.entities.Observation;
import org.clprolf.weatherapp.repos.ObservationDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ClAgent
public class WeatherAgentImpl implements WeatherAgent {

    private final ObservationDao obsDao;

    public WeatherAgentImpl(ObservationDao obsDao) {
        this.obsDao = obsDao;
    }

    public List<Observation> getObsByStation(String station) {
        List<Observation> observations = obsDao.findByIdStation(station);
        return observations;
    }

    public Observation newObservation(Observation observation){
        Observation savedObservation = obsDao.save(observation);
        return savedObservation;
    }

    public List<Observation> getAllObs(){
        List<Observation> allObservations = obsDao.findAll();
        return allObservations;
    }
}
