package org.clprolf.weatherapp.repos;

import java.util.List;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;
import org.clprolf.weatherapp.entities.Observation;
import org.clprolf.weatherapp.entities.ObservationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@ClWorker
@ClFamily
@Repository
public interface ObservationDao extends JpaRepository<Observation, ObservationId> {
    List<Observation> findByIdStation(String station);

}
