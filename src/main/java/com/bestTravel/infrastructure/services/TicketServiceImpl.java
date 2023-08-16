package com.bestTravel.infrastructure.services;

import com.bestTravel.api.model.request.TicketRequest;
import com.bestTravel.api.model.response.FlyResponse;
import com.bestTravel.api.model.response.TicketResponse;
import com.bestTravel.domain.entity.TicketEntity;
import com.bestTravel.domain.repository.CustomerRepository;
import com.bestTravel.domain.repository.FlyRepository;
import com.bestTravel.domain.repository.TicketRepository;
import com.bestTravel.infrastructure.abstract_services.TicketService;
import com.bestTravel.infrastructure.helpers.BlackListHelper;
import com.bestTravel.infrastructure.helpers.CustomerHelper;
import com.bestTravel.util.BestTravelUtil;
import com.bestTravel.util.enums.Tables;
import com.bestTravel.util.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;


    public static final BigDecimal CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);

    @Override
    public TicketResponse create(TicketRequest request) {
        log.info("---> inicio servicio crear ticket");
        log.info("---> buscando vuelo...");
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly())
                .orElseThrow(() -> new  IdNotFoundException(Tables.fly.name()));
        log.info("---> buscando cliente...");
        var customer = customerRepository.findById(request.getIdClient())
                .orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));
        log.info("---> guardando ticket...");
        var ticketPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .departureDate(BestTravelUtil.getRandomSoom())
                .build();
        var ticket = ticketRepository.save(ticketPersist);
        customerHelper.incrase(customer.getDni(), TicketService.class);
        log.info("--> guardado ticket id {}", ticket.getId());
        log.info("---> finalizado servicio guardar ticket");
        return this.mapToDTO(ticket);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        log.info("---> inicio servicio buscar ticket por id");
        log.info("---> buscando ticket con id: {}", uuid);
        var ticket = ticketRepository.findById(uuid)
                .orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        log.info("---> finalizado servicio buscar ticket");
        return mapToDTO(ticket);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        log.info("---> inicio servicio actualizar ticket");
        log.info("---> buscando ticket con id: {}", uuid);
        var tickeBase = ticketRepository.findById(uuid)
                .orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        var fly = flyRepository.findById(request.getIdFly())
                .orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));
        log.info("---> actualizando ticket");
        tickeBase.setFly(fly);
        tickeBase.setPrice(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)));
        tickeBase.setDepartureDate(BestTravelUtil.getRandomSoom());
        tickeBase.setDepartureDate(BestTravelUtil.getRandomLatter());
        var ticketUpdate = ticketRepository.save(tickeBase);
        log.info("---> finalizado servicio actualizar ticket");
        return mapToDTO(ticketUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        log.info("---> inicio servicio eliminar ticket");
        log.info("---> buscando ticket con id {}", uuid);
        ticketRepository.findById(uuid)
                .orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        log.info("---> eliminando ticket con id {}", uuid);
        ticketRepository.deleteById(uuid);
        log.info("---> finalizado servicio eliminar ticket");
    }

    @Override
    public BigDecimal findPrice(Long idFly) {
        log.info("---> inicio servicio obtener precio de ticket");
        log.info("---> obteniendo vuelo...");
        var fly = flyRepository.findById(idFly)
                .orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));
        log.info("---> finalizado servicio obtener precio de ticket");
        return fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE));
    }

    private TicketResponse mapToDTO(TicketEntity entity){
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }


}
