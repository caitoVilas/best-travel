package com.bestTravel.api.controller;

import com.bestTravel.api.model.request.TourRequest;
import com.bestTravel.api.model.response.TourResponse;
import com.bestTravel.infrastructure.abstract_services.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@RestController
@RequestMapping("/tours")
@Slf4j
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @PostMapping
    public ResponseEntity<TourResponse> create(@RequestBody TourRequest request){
        log.info("#### endpoint creacion de tours");
        return ResponseEntity.ok(tourService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourResponse> read(@PathVariable Long id){
        log.info("#### endpoint buscar tour por id");
        return ResponseEntity.ok(tourService.read(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        log.info("#### endpoint eliminar tour por id");
        tourService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/remove-ticket/{ticketId}/{tourId}")
    public ResponseEntity<Void> removeTcket(@PathVariable UUID ticketId,
                                            @PathVariable Long tourId){
        log.info("#### endpoint remover ticket de tour");
        tourService.removeTicket(ticketId, tourId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/add-ticket/{flyId}/{ticketId}")
    public ResponseEntity<Map<String,UUID>> addTicket(@PathVariable Long flyId, @PathVariable Long ticketId){
        log.info("#### endpoint agregar ticket a tour");
        var response = tourService.addTicket(flyId, ticketId);
        return ResponseEntity.ok(Collections.singletonMap("ticket id", response));
    }

    @PatchMapping("/remove-reservation/{reservationId}/{tourId}")
    public ResponseEntity<Void> removeReservation(@PathVariable UUID reservationId,
                                                  @PathVariable Long tourId){
        log.info("##### endpoint remover reservacion de tour");
        tourService.removeReservation(reservationId, tourId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/add-reservation/{hotelId}/{tourId}/{totalDays}")
    public ResponseEntity<Map<String, UUID>> addReservation(@PathVariable Long hotelId,
                                                            @PathVariable Long tourId,
                                                            @PathVariable Integer totalDays){
        log.info("#### endpoint agregar reservacion a tour");
        var response = tourService.addReservation(hotelId, tourId, totalDays);
        return ResponseEntity.ok(Collections.singletonMap("reservation id", response));
    }
}
