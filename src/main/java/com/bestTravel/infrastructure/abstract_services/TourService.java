package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.api.model.request.TourRequest;
import com.bestTravel.api.model.response.TourResponse;

import java.util.UUID;

/**
 * @author claudio.vilas
 * date 08/2023
 */

public interface TourService extends SimpleCrudService<TourRequest, TourResponse, Long> {
    Void removeTicket(UUID ticketId, Long tourId);
    UUID addTicket(Long fliyId, Long tourId);
    void removeReservation(UUID reservationId, Long tourId);
    UUID addReservation(Long hotelId, Long tourId, Integer totalDays);
}
