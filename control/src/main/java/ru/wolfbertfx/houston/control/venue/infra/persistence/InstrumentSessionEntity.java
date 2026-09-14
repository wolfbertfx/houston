package ru.wolfbertfx.houston.control.venue.infra.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.control.shared.infra.persistence.InstrumentConverter;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "instrument_sessions")
public class InstrumentSessionEntity extends PanacheEntity {

    @Convert(converter = VenueConverter.class)
    @Column(name = "venue_id", nullable = false)
    private Venue venue;

    @Convert(converter = InstrumentConverter.class)
    @Column(name = "instrument_id")
    private Instrument instrument;

    @Convert(converter = TypeConverter.class)
    @Column(name = "type_id")
    private Type type;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    @Convert(converter = PhaseConverter.class)
    @Column(name = "phase_id", nullable = false)
    private Phase phase;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public Instrument getInstrument() { return instrument; }
    public void setInstrument(Instrument instrument) { this.instrument = instrument; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }
    public LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(LocalTime openTime) { this.openTime = openTime; }
    public LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalTime closeTime) { this.closeTime = closeTime; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentSessionEntity that)) return false;
        return venue == that.venue
                && Objects.equals(instrument, that.instrument)
                && Objects.equals(type, that.type)
                && dayOfWeek == that.dayOfWeek
                && phase == that.phase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, instrument, type, dayOfWeek, phase);
    }
}