package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.api.model.request.ReservationRequest;
import com.bestTravel.api.model.response.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

public interface ReservationService extends CrudServices<ReservationRequest, ReservationResponse, UUID> {
    BigDecimal findPrice(Long idHotel, Currency currency);
}
