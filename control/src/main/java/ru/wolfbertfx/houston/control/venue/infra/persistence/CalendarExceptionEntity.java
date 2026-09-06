package ru.wolfbertfx.houston.control.venue.infra.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.shared.infra.persistence.InstrumentConverter;
import ru.wolfbertfx.houston.control.venue.domain.ExceptionType;

import java.time.LocalDate;

@Entity
@Table(name = "calendar_exceptions")
public class CalendarExceptionEntity extends PanacheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = VenueConverter.class)
    @Column(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Convert(converter = InstrumentConverter.class)
    @Column(name = "instrument_id")
    private Instrument instrument;

    @Convert(converter = TypeConverter.class)
    @Column(name = "instrument_type_id")
    private Type instrumentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "exception_type", nullable = false)
    private ExceptionType type;

    @Column(name = "description", length = 255)
    private String description;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Instrument getInstrument() { return instrument; }
    public void setInstrument(Instrument instrument) { this.instrument = instrument; }
    public Type getInstrumentType() { return instrumentType; }
    public void setInstrumentType(Type instrumentType) { this.instrumentType = instrumentType; }
    public ExceptionType getType() { return type; }
    public void setType(ExceptionType type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarExceptionEntity that)) return false;
        return java.util.Objects.equals(venue, that.venue)
                && java.util.Objects.equals(startDate, that.startDate)
                && java.util.Objects.equals(endDate, that.endDate)
                && java.util.Objects.equals(instrument, that.instrument)
                && java.util.Objects.equals(instrumentType, that.instrumentType);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(venue, startDate, endDate, instrument, instrumentType);
    }
}