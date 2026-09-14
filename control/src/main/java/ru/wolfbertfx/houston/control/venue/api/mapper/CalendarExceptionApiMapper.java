package ru.wolfbertfx.houston.control.venue.api.mapper;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.venue.api.dto.CalendarExceptionRequest;
import ru.wolfbertfx.houston.control.venue.api.dto.CalendarExceptionResponse;
import ru.wolfbertfx.houston.control.venue.domain.CalendarException;

import java.util.List;
import java.util.stream.Collectors;

public final class CalendarExceptionApiMapper {

    private CalendarExceptionApiMapper() {}

    public static CalendarException toDomain(CalendarExceptionRequest req) {
        return new CalendarException(
                null,
                Venue.fromId(req.venueId()),
                req.startDate(),
                req.endDate(),
                req.instrumentId() != null ? Instrument.fromId(req.instrumentId()) : null,
                req.typeId() != null ? Type.fromId(req.typeId()) : null,
                req.type(),
                req.description()
        );
    }

    public static CalendarException toDomain(CalendarExceptionRequest req, long id) {
        return new CalendarException(
                id,
                Venue.fromId(req.venueId()),
                req.startDate(),
                req.endDate(),
                req.instrumentId() != null ? Instrument.fromId(req.instrumentId()) : null,
                req.typeId() != null ? Type.fromId(req.typeId()) : null,
                req.type(),
                req.description()
        );
    }

    public static CalendarExceptionResponse toResponse(ru.wolfbertfx.houston.control.venue.domain.CalendarException exc) {
        return new CalendarExceptionResponse(
                exc.id(),
                exc.venue().getId(),
                exc.venue().name(),
                exc.startDate(),
                exc.endDate(),
                exc.instrument() != null ? exc.instrument().getId() : null,
                exc.instrument() != null ? exc.instrument().getSymbol() : null,
                exc.instrumentType() != null ? exc.instrumentType().getId() : null,
                exc.instrumentType() != null ? exc.instrumentType().name() : null,
                exc.type(),
                exc.description()
        );
    }

    public static List<CalendarExceptionResponse> toResponse(List<ru.wolfbertfx.houston.control.venue.domain.CalendarException> exceptions) {
        return exceptions.stream()
                .map(CalendarExceptionApiMapper::toResponse)
                .collect(Collectors.toList());
    }
}