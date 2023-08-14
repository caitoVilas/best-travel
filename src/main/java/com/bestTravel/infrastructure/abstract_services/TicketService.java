package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.api.model.request.TicketRequest;
import com.bestTravel.api.model.response.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */
public interface TicketService extends CrudServices<TicketRequest, TicketResponse, UUID> {
    BigDecimal findPrice(Long idFly);
}
