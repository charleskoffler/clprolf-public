package org.clprolf.weatherapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import jakarta.validation.constraints.DecimalMin;
import org.clprolf.framework.ClAgent;

import java.math.BigDecimal;


@ClAgent
@Entity
public class Observation {
    @EmbeddedId
    private ObservationId id = new ObservationId();
    @DecimalMin("0.0")
    @Column(precision=4, scale=1, nullable = true)
    private BigDecimal maxGust;

    @Column(precision=4, scale=1, nullable = true)
    private BigDecimal tmin;

    @Column(precision=4, scale=1, nullable = true)
    private BigDecimal tmax;

    public ObservationId getId() {
        return id;
    }

    public @DecimalMin("0.0") BigDecimal getMaxGust() {
        return maxGust;
    }
    public BigDecimal getTmin() {
        return tmin;
    }
    public BigDecimal getTmax() {
        return tmax;
    }

}
