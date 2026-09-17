package ru.wolfbertfx.houston.control.venue.infra.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import ru.wolfbertfx.houston.common.asset.Form;
import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.control.shared.infra.persistence.TickerConverter;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "instrument_sessions")
public class InstrumentSessionEntity extends PanacheEntity {

    @Convert(converter = VenueConverter.class)
    @Column(name = "venue_id", nullable = false)
    private Venue venue;

    @Convert(converter = TickerConverter.class)
    @Column(name = "ticker_id")
    private Ticker ticker;

    @Convert(converter = TypeConverter.class)
    @Column(name = "type_id")
    private Form form;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public Ticker getTicker() { return ticker; }
    public void setTicker(Ticker ticker) { this.ticker = ticker; }
    public Form getForm() { return form; }
    public void setForm(Form form) { this.form = form; }
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
        return venue == that.venue && Objects.equals(ticker, that.ticker)
                && Objects.equals(form, that.form) && dayOfWeek == that.dayOfWeek && phase == that.phase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, ticker, form, dayOfWeek, phase);
    }
}