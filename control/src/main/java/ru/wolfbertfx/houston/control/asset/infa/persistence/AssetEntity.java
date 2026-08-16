package ru.wolfbertfx.houston.control.asset.infa.persistence;

import jakarta.persistence.*;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.asset.Ticker;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "assets")
class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TickerConverter.class)
    @Column(name = "ticker_id", nullable = false, unique = true)
    private Ticker ticker;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status_id", nullable = false)
    private Status status = Status.DISABLED;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {this.lastUpdated = Instant.now();}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Ticker getTicker() {return ticker;}
    public void setTicker(Ticker ticker) {this.ticker = ticker;}
    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}
    public Long getVersion() {return version;}
    public void setVersion(Long version) {this.version = version;}
    public Instant getLastUpdated() {return lastUpdated;}
    public void setLastUpdated(Instant lastUpdated) {this.lastUpdated = lastUpdated;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetEntity that)) return false;
        return ticker != null && ticker.equals(that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }
}
