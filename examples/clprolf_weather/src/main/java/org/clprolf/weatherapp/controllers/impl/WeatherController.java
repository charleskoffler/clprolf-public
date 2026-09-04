package org.clprolf.weatherapp.controllers.impl;

import jakarta.validation.Valid;
import org.clprolf.framework.ClSystem;
import org.clprolf.weatherapp.agents.services.WeatherAgent;
import org.clprolf.weatherapp.agents.services.impl.WeatherAgentImpl;
import org.clprolf.weatherapp.entities.Observation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ClSystem
@RestController
@RequestMapping("/weatherapi/observations")
public class WeatherController {

    private final WeatherAgent agent;

    public WeatherController(WeatherAgent agent) {
        this.agent = agent;
    }

    @GetMapping("get/station/{station}")
    public ResponseEntity<List<Observation>> getObsByStation(@PathVariable String station){

        List<Observation> observations = agent.getObsByStation(station);
        return observations.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(observations);
    }

    @PostMapping("create")
    public ResponseEntity<Observation> newObservation(@Valid @RequestBody Observation observation) {

        Observation savedObservation = agent.newObservation(observation);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedObservation);
    }

    @GetMapping("all")
    public ResponseEntity<List<Observation>> getAllObs() {
        List<Observation> allObservations = agent.getAllObs();

        return allObservations.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(allObservations);
    }
}
