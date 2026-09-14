package ru.wolfbertfx.houston.control.venue.api.mapper;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.control.venue.api.dto.VenueSessionRequest;
import ru.wolfbertfx.houston.control.venue.api.dto.VenueSessionResponse;
import ru.wolfbertfx.houston.control.venue.domain.VenueSession;

import java.util.List;
import java.util.stream.Collectors;

public final class VenueSessionApiMapper {

    private VenueSessionApiMapper() {}

    public static VenueSession toDomain(VenueSessionRequest req) {
        return new VenueSession(
                null,
                Venue.fromId(req.venueId()),
                req.dayOfWeek(),
                Phase.fromId(req.phaseId()),
                req.openTime(),
                req.closeTime()
        );
    }

    public static VenueSession toDomain(VenueSessionRequest req, long id) {
        return new VenueSession(
                id,
                Venue.fromId(req.venueId()),
                req.dayOfWeek(),
                Phase.fromId(req.phaseId()),
                req.openTime(),
                req.closeTime()
        );
    }

    public static VenueSessionResponse toResponse(VenueSession session) {
        return new VenueSessionResponse(
                session.id(),
                session.venue().getId(),
                session.venue().name(),
                session.dayOfWeek(),
                session.phase().getId(),
                session.phase().name(),
                session.openTime(),
                session.closeTime(),
                session.isOvernight()
        );
    }

    public static List<VenueSessionResponse> toResponse(List<VenueSession> sessions) {
        return sessions.stream()
                .map(VenueSessionApiMapper::toResponse)
                .collect(Collectors.toList());
    }
}