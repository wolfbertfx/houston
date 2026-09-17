package ru.wolfbertfx.houston.control.asset.infra.persistence;

import jakarta.persistence.*;
import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.control.shared.infra.persistence.TickerConverter;
import java.util.Objects;

@Entity
@Table(name = "assets")

class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TickerConverter.class)
    @Column(name = "ticker_id")
    private Ticker ticker;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status_id")
    private Status status = Status.DISABLED;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Ticker getTicker() {return ticker;}
    public void setTicker(Ticker instrument) {this.ticker = instrument;}
    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}
    public Long getVersion() {return version;}
    public void setVersion(Long version) {this.version = version;}

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