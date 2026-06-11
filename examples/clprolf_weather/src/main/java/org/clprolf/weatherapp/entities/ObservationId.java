package org.clprolf.weatherapp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.clprolf.framework.ClAgent;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@ClAgent
@Embeddable
public class ObservationId {

    @Column(length = 8)
    private String station;

    // Si on veut YYYYMMDD @JsonFormat( pattern="yyyyMMdd" )
    private OffsetDateTime dayUTC;

    public String getStation() {
        return station;
    }

    public OffsetDateTime getDayUTC() {
        return dayUTC;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObservationId that = (ObservationId) o;
        return Objects.equals(station, that.station) && Objects.equals(dayUTC, that.dayUTC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, dayUTC);
    }

}
