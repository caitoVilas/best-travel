package com.bestTravel.infrastructure.services;

import com.bestTravel.api.model.request.TourRequest;
import com.bestTravel.api.model.response.TourResponse;
import com.bestTravel.domain.entity.*;
import com.bestTravel.domain.repository.CustomerRepository;
import com.bestTravel.domain.repository.FlyRepository;
import com.bestTravel.domain.repository.HotelRepository;
import com.bestTravel.domain.repository.TourRepository;
import com.bestTravel.infrastructure.abstract_services.TourService;
import com.bestTravel.infrastructure.helpers.BlackListHelper;
import com.bestTravel.infrastructure.helpers.CustomerHelper;
import com.bestTravel.infrastructure.helpers.TourHelper;
import com.bestTravel.util.enums.Tables;
import com.bestTravel.util.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author claudio.vilas
 * date. 08/2023
 */

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;

    @Override
    public TourResponse create(TourRequest request) {
        log.info("---> iniciando servicio crear tour");
        log.info("---> buscando cliente id {}", request.getCustomerId());
        blackListHelper.isInBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));
        log.info("---> buscando vuelos...");
        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> flights.add(flyRepository.findById(fly.getId())
                .orElseThrow(()-> new IdNotFoundException(Tables.fly.name()))));
        log.info("---> buscando hoteles...");
        var hotels = new  HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(hotelRepository.findById(hotel.getId())
                .orElseThrow(()-> new IdNotFoundException(Tables.hotel.name())),hotel.getTotalDays()));
        log.info("---> generando tour...");
        var tour = TourEntity.builder()
                .tickets(tourHelper.createTicket(flights,customer))
                .reservations(tourHelper.createReservation(hotels, customer))
                .customer(customer)
                .build();
        var tourNew = tourRepository.save(tour);
        customerHelper.incrase(customer.getDni(), TourService.class);
        log.info("---> finalizado servicio crear tour");
        return TourResponse.builder()
                .reservationIds(tourNew.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIds(tourNew.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tour.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        log.info("---> iniciando servicio buscar tour por id");
        log.info("---> buscando tour id {}", id);
        var tour = tourRepository.findById(id)
                .orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        log.info("---> finalizado servicio buscar tour por id");
        return TourResponse.builder()
                .id(tour.getId())
                .reservationIds(tour.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIds(tour.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void delete(Long id) {
        log.info("---> iniciando servicio eliminar tour por id");
        log.info("---> eliminando tour id {}", id);
        TourEntity tour = tourRepository.findById(id)
                .orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        tourRepository.delete(tour);
    }

    @Override
    public Void removeTicket(UUID ticketId, Long tourId) {
        log.info("---> inicio servicio remover ticket de tour");
        log.info("---> buscando tour id {}", ticketId);
        var tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        tour.removeTicket(ticketId);
        tourRepository.save(tour);
        log.info("---> finalizado servicio remover ticket de tour");
        return null;
    }

    @Override
    public UUID addTicket(Long fliyId, Long tourId) {
        log.info("---> inicio servicio agregar ticket a tour");
        log.info("---> buscando tour id {}", tourId);
        var tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        log.info("---> buscando vuelo id {}", fliyId);
        var fly = flyRepository.findById(fliyId)
                .orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));
        log.info("---> procesando tour...");
        var ticket = tourHelper.createTicket(fly, tour.getCustomer());
        tour.addTicket(ticket);
        tourRepository.save(tour);
        log.info("---> finalizado servicio agregar ticket a tour");
        return ticket.getId();
    }

    @Override
    public void removeReservation(UUID reservationId, Long tourId) {
        log.info("---> inicio servicio remover reservacion de tour");
        log.info("---> buscando tour con id {}", tourId);
        var tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        log.info("---> removiendo reservacion...");
        tour.removeReservation(reservationId);
        tourRepository.save(tour);
        log.info("---> finalizado servicio remover reservacion de tour");
    }

    @Override
    public UUID addReservation(Long hotelId, Long tourId, Integer totalDays) {
        log.info("---> inicio servicio agregar reservacion a tour");
        log.info("---> buscando tor con id {}", tourId);
        var tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        log.info("---> buscando hotel con id {}", hotelId);
        var hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        log.info("---> procesando tour...");
        var reservation = tourHelper.createReservation(hotel, tour.getCustomer(), totalDays);
        tour.addReservation(reservation);
        tourRepository.save(tour);
        log.info("---> finalizado servicio agregar reservacion a tour");
        return reservation.getId();
    }
}
