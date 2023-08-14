package com.bestTravel.infrastructure.services;

import com.bestTravel.api.model.request.TicketRequest;
import com.bestTravel.api.model.response.FlyResponse;
import com.bestTravel.api.model.response.TicketResponse;
import com.bestTravel.domain.entity.TicketEntity;
import com.bestTravel.domain.repository.CustomerRepository;
import com.bestTravel.domain.repository.FlyRepository;
import com.bestTravel.domain.repository.TicketRepository;
import com.bestTravel.infrastructure.abstract_services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketResponse create(TicketRequest request) {
        log.info("---> inicio servicio crear ticket");
        log.info("---> buscando vuelo...");
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        log.info("---> buscando cliente...");
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();
        log.info("---> guardando ticket...");
        var ticketPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(0.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        var ticket = ticketRepository.save(ticketPersist);
        log.info("--> guardado ticket id {}", ticket.getId());
        log.info("---> finalizado servicio guardar ticket");
        return this.mapToDTO(ticket);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        log.info("---> inicio servicio buscar ticket por id");
        log.info("---> buscando ticket con id: {}", uuid);
        var ticket = ticketRepository.findById(uuid).orElseThrow();
        log.info("---> finalizado servicio buscar ticket");
        return mapToDTO(ticket);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        log.info("---> inicio servicio actualizar ticket");
        log.info("---> buscando ticket con id: {}", uuid);
        var tickeBase = ticketRepository.findById(uuid).orElseThrow();
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        log.info("---> actualizando ticket");
        tickeBase.setFly(fly);
        tickeBase.setPrice(fly.getPrice().multiply(BigDecimal.valueOf(0.25)));
        tickeBase.setDepartureDate(LocalDateTime.now());
        tickeBase.setDepartureDate(LocalDateTime.now());
        var ticketUpdate = ticketRepository.save(tickeBase);
        log.info("---> finalizado servicio actualizar ticket");
        return mapToDTO(ticketUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        log.info("---> inicio servicio eliminar ticket");
        log.info("---> buscando ticket con id {}", uuid);
        var ticket = ticketRepository.findById(uuid).orElseThrow();
        log.info("---> eliminando ticket con id {}", uuid);
        ticketRepository.deleteById(uuid);
        log.info("---> finalizado servicio eliminar ticket");
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
