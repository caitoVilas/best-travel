package com.bestTravel.api.controller;

import com.bestTravel.api.model.request.ReservationRequest;
import com.bestTravel.api.model.response.ReservationResponse;
import com.bestTravel.infrastructure.abstract_services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Best Travel - Reservaciones")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "servicio que crea una reservacion", description = "servicio que crea una reservacion")
    @Parameter(name = "request", description = "datos de reservacion")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request){
        log.info("#### endpoint creacion de reservaciones");
        return ResponseEntity.ok(reservationService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "muestra una reservacion po id",
            description = "muestra una reservacion po id si existe")
    @Parameter(name = "id", description = "id de reservacion")
    public ResponseEntity<ReservationResponse> read(@PathVariable UUID id){
        log.info("#### endpoint mostrar reservacion por id");
        return ResponseEntity.ok(reservationService.read(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "servicio para actualizar una reservacion",
            description = "servicio para actualizar una reservacion")
    @Parameters({
            @Parameter(name = "id", description = "id de la reserva"),
            @Parameter(name = "request", description = "datos de la reservacion")
    })
    public ResponseEntity<ReservationResponse> update(@Valid @PathVariable UUID id,
                                                      @RequestBody ReservationRequest request){
        log.info("#### endpoint actualizar reservacion");
        return ResponseEntity.ok(reservationService.update(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "servicio para elimina una reservacion por id",
            description = "servicio para elimina una reservacion por id")
    @Parameter(name = "id", description = "id de la reservacion")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        log.info("#### endpoint eliminar reservacion por id");
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/price/{hotelId}")
    @Operation(summary = "muestra el precio de un hotel por si id",
            description = "muestra el precio de un hotel por si id")
    @Parameter(name = "hotelId", description = "id del hotel")
    public ResponseEntity<Map<String, BigDecimal>> viewPrice(@PathVariable Long hotelId){
        log.info("#### endpoint ver precio de reservacion por id de hotel");
        return ResponseEntity.ok(Collections.singletonMap("Precio", reservationService.findPrice(hotelId)));
    }
}
