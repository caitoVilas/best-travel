package com.bestTravel.api.controller;

import com.bestTravel.api.model.request.ReservationRequest;
import com.bestTravel.api.model.response.ReservationResponse;
import com.bestTravel.infrastructure.abstract_services.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@Slf4j
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest request){
        log.info("#### endpoint creacion de reservaciones");
        return ResponseEntity.ok(reservationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> read(@PathVariable UUID id){
        log.info("#### endpoint mostrar reservacion por id");
        return ResponseEntity.ok(reservationService.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable UUID id,
                                                      @RequestBody ReservationRequest request){
        log.info("#### endpoint actualizar reservacion");
        return ResponseEntity.ok(reservationService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        log.info("#### endpoint eliminar reservacion por id");
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/price/{hotelId}")
    public ResponseEntity<Map<String, BigDecimal>> viewPrice(@PathVariable Long hotelId){
        log.info("#### endpoint ver precio de reservacion por id de hotel");
        return ResponseEntity.ok(Collections.singletonMap("Precio", reservationService.findPrice(hotelId)));
    }
}
