package org.clprolf.weatherapp.agents.services;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.weatherapp.entities.Observation;
import org.springframework.stereotype.Service;

import java.util.List;

@ClFamily @ClAgent
public interface WeatherAgent {
    List<Observation> getObsByStation(String station);
    Observation newObservation(Observation observation);
    List<Observation> getAllObs();
}
