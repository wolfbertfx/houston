package ru.wolfbertfx.houston.control.venue.infra.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;

import java.util.Objects;

@Entity
@Table(name = "venue_sessions")
public class VenueSessionEntity extends PanacheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = VenueConverter.class)
    @Column(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    @Convert(converter = PhaseConverter.class)
    @Column(name = "phase_id", nullable = false)
    private Phase phase;

    @Column(name = "open_time", nullable = false)
    private java.time.LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private java.time.LocalTime closeTime;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }
    public java.time.LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(java.time.LocalTime openTime) { this.openTime = openTime; }
    public java.time.LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(java.time.LocalTime closeTime) { this.closeTime = closeTime; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VenueSessionEntity that)) return false;
        return venue == that.venue && dayOfWeek == that.dayOfWeek && phase == that.phase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, dayOfWeek, phase);
    }
}