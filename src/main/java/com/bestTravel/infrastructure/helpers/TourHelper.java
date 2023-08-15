package com.bestTravel.infrastructure.helpers;

import com.bestTravel.domain.entity.*;
import com.bestTravel.domain.repository.ReservationRepository;
import com.bestTravel.domain.repository.TicketRepository;
import com.bestTravel.infrastructure.services.ReservationServiceImpl;
import com.bestTravel.infrastructure.services.TicketServiceImpl;
import com.bestTravel.util.BestTravelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TourHelper {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTicket(Set<FlyEntity> flights, CustomerEntity customer){
        var response =new HashSet<TicketEntity>(flights.size());
        flights.forEach(fly ->{
            var ticket = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer(customer)
                    .price(fly.getPrice().add(fly.getPrice().multiply(TicketServiceImpl.CHARGER_PRICE_PERCENTAGE)))
                    .purchaseDate(LocalDate.now())
                    .departureDate(BestTravelUtil.getRandomSoom())
                    .arrivalDate(BestTravelUtil.getRandomLatter())
                    .build();
            response.add(ticketRepository.save(ticket));
        });
        return response;
    }

    public Set<ReservationEntity> createReservation(HashMap<HotelEntity,Integer> hotels, CustomerEntity customer){
        var response = new HashSet<ReservationEntity>(hotels.size());
        hotels.forEach((hotel, totaldays)->{
            var reservation = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer(customer)
                    .totalDays(totaldays)
                    .dateTimeReservation(LocalDateTime.now())
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(totaldays))
                    .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationServiceImpl.CHARGES_PRICE_PERCENTAGE)))
                    .build();
            response.add(reservationRepository.save(reservation));
        });
        return response;
    }

    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer){
        var ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(TicketServiceImpl.CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoom())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .build();
        return ticketRepository.save(ticket);
    }

    public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer,
                                               Integer totalDays){
        var reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationServiceImpl.CHARGES_PRICE_PERCENTAGE)))
                .build();
        return reservationRepository.save(reservation);
    }
}
