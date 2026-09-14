package ru.wolfbertfx.houston.control.venue.api.mapper;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.control.venue.api.dto.InstrumentSessionRequest;
import ru.wolfbertfx.houston.control.venue.api.dto.InstrumentSessionResponse;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSession;

import java.util.List;
import java.util.stream.Collectors;

public final class InstrumentSessionApiMapper {

    private InstrumentSessionApiMapper() {}

    public static InstrumentSession toDomain(InstrumentSessionRequest req) {
        Venue venue = Venue.fromId(req.venueId());
        Instrument instrument = req.instrumentId() != null ? Instrument.fromId(req.instrumentId()) : null;
        Type type = req.typeId() != null ? Type.fromId(req.typeId()) : null;
        return new InstrumentSession(
                null,
                Venue.fromId(req.venueId()),
                instrument,
                type,
                req.dayOfWeek(),
                ru.wolfbertfx.houston.common.venue.Phase.fromId(req.phaseId()),
                req.openTime(),
                req.closeTime()
        );
    }

    public static InstrumentSession toDomain(InstrumentSessionRequest req, long id) {
        Venue venue = Venue.fromId(req.venueId());
        Instrument instrument = req.instrumentId() != null ? Instrument.fromId(req.instrumentId()) : null;
        Type type = req.typeId() != null ? Type.fromId(req.typeId()) : null;
        return new InstrumentSession(
                id,
                Venue.fromId(req.venueId()),
                instrument,
                type,
                req.dayOfWeek(),
                ru.wolfbertfx.houston.common.venue.Phase.fromId(req.phaseId()),
                req.openTime(),
                req.closeTime()
        );
    }

    public static InstrumentSessionResponse toResponse(ru.wolfbertfx.houston.control.venue.domain.InstrumentSession session) {
        return new InstrumentSessionResponse(
                session.id(),
                session.venue().getId(),
                session.venue().name(),
                session.instrument() != null ? session.instrument().getId() : null,
                session.instrument() != null ? session.instrument().getSymbol() : null,
                session.type() != null ? session.type().getId() : null,
                session.type() != null ? session.type().name() : null,
                session.dayOfWeek(),
                session.phase().getId(),
                session.phase().name(),
                session.openTime(),
                session.closeTime(),
                session.isOvernight()
        );
    }

    public static List<InstrumentSessionResponse> toResponse(List<ru.wolfbertfx.houston.control.venue.domain.InstrumentSession> sessions) {
        return sessions.stream()
                .map(InstrumentSessionApiMapper::toResponse)
                .collect(Collectors.toList());
    }
}