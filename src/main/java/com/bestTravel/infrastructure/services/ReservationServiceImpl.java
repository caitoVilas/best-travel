package com.bestTravel.infrastructure.services;

import com.bestTravel.api.model.request.ReservationRequest;
import com.bestTravel.api.model.response.HotelResponse;
import com.bestTravel.api.model.response.ReservationResponse;
import com.bestTravel.domain.entity.ReservationEntity;
import com.bestTravel.domain.repository.CustomerRepository;
import com.bestTravel.domain.repository.HotelRepository;
import com.bestTravel.domain.repository.ReservationRepository;
import com.bestTravel.infrastructure.abstract_services.ReservationService;
import com.bestTravel.infrastructure.helpers.ApiCurrencyConectorHelper;
import com.bestTravel.infrastructure.helpers.BlackListHelper;
import com.bestTravel.infrastructure.helpers.CustomerHelper;
import com.bestTravel.infrastructure.helpers.EmailHelper;
import com.bestTravel.util.enums.Tables;
import com.bestTravel.util.exception.IdNotFoundException;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConectorHelper apiCurrencyConectorHelper;
    private final EmailHelper emailHelper;

    public static final BigDecimal CHARGES_PRICE_PERCENTAGE = BigDecimal.valueOf(0.20);

    @Override
    public ReservationResponse create(ReservationRequest request) {
        log.info("---> inicio servicio guardar reservacion");
        log.info("---> buscando hotel id {}", request.getIdHotel());
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = hotelRepository.findById(request.getIdHotel())
                .orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        log.info("---> buscando cliente {}", request.getIdClient());
        var customer = customerRepository.findById(request.getIdClient())
                .orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));
        var reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGE)))
                .build();
        log.info("---> guardando reservacion...");
        var reservationNew = reservationRepository.save(reservation);
        customerHelper.incrase(customer.getDni(), ReservationService.class);
        if (Objects.nonNull(request.getEmail())) emailHelper.sendMail(request.getEmail(),
                customer.getFullName(),Tables.reservation.name());
        log.info("---> finalizado servicio guardar reservacion");
        return this.mapToDto(reservationNew);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        log.info("---> inicio servcio buscar reservacion por id");
        log.info("---> buscando reservacion id {}", uuid);
        var reservation = reservationRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        log.info("---> finalizado servicio buscar reservacion por id");
        return this.mapToDto(reservation);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID uuid) {
        log.info("---> inicio servicio actualizar reservacion");
        log.info("---> buscando reservacion id {}", uuid);
        var reservation = reservationRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        log.info("---> buscando hotel id {}", request.getIdHotel());
        var  hotel = hotelRepository.findById(request.getIdHotel())
                .orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        log.info("---> actualizando reservacion...");
        reservation.setHotel(hotel);
        reservation.setTotalDays(request.getTotalDays());
        reservation.setDateTimeReservation(LocalDateTime.now());
        reservation.setDateStart(LocalDate.now());
        reservation.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationRepository.save(reservation);
        reservation.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGE)));
        log.info("---> finalizado servicio actualizacion reservacion");
        return this.mapToDto(reservation);
    }

    @Override
    public void delete(UUID uuid) {
        log.info("---> inicio servicio eliminar reservacion por id");
        log.info("---> buscando reservacion id {}", uuid);
        reservationRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        log.info("---> eliminando reservacion...");
        reservationRepository.deleteById(uuid);
        log.info("---> finalizado servicio eliminar por id");

    }

    @Override
    public BigDecimal findPrice(Long idHotel, Currency currency) {
        log.info("---> inicio servicio buscar precio reservacion");
        log.info("---> buscando hotel {}", idHotel);
        var hotel = hotelRepository.findById(idHotel)
                .orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        var priceInDolar = hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGE));
        if (currency.equals(Currency.getInstance("USD"))) return priceInDolar;
        var currencyDto = apiCurrencyConectorHelper.getCurrency(currency);
        log.info("---> API  currency in {}, response {}",currencyDto.getExchangeDate().toString(), currencyDto.getRates());
        log.info("---> finalizado servicio buscar precio reservacion");
        return priceInDolar.multiply(currencyDto.getRates().get(currency));
    }

    private ReservationResponse mapToDto(ReservationEntity reservation){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(reservation, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(reservation.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }


}
