package org.clprolf.weatherapp.controllers;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.weatherapp.entities.Observation;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ClAgent
@ClFamily
public interface ClprolfWeatherController {
    ResponseEntity<List<Observation>> getObsByStation(String station);

    ResponseEntity<Observation> createObservation(Observation observation);
    ResponseEntity<List<Observation>> getAllObs();
}
