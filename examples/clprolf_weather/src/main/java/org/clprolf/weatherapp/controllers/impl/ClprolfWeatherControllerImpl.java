package org.clprolf.weatherapp.controllers.impl;

import jakarta.validation.Valid;
import org.clprolf.framework.ClAgent;
import org.clprolf.weatherapp.controllers.ClprolfWeatherController;
import org.clprolf.weatherapp.entities.Observation;
import org.clprolf.weatherapp.repos.ObservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ClAgent
@RestController
@RequestMapping("/weatherapi/observations")
public class ClprolfWeatherControllerImpl implements ClprolfWeatherController {

    @Autowired
    private ObservationDao obsDao;

    @GetMapping("get/station/{station}")
    public ResponseEntity<List<Observation>> getObsByStation(@PathVariable String station){

        List<Observation> observations = obsDao.findByIdStation(station);
        return observations.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(observations);
    }

    @PostMapping("create")
    @Override
    public ResponseEntity<Observation> createObservation(@Valid @RequestBody Observation observation) {

        Observation savedObservation = obsDao.save(observation);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedObservation);
    }

    @GetMapping("all")
    @Override
    public ResponseEntity<List<Observation>> getAllObs() {
        List<Observation> allObservations = obsDao.findAll();

        return allObservations.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(allObservations);
    }
}
