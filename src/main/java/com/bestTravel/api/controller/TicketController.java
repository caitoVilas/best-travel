package com.bestTravel.api.controller;

import com.bestTravel.api.model.request.TicketRequest;
import com.bestTravel.api.model.response.ErrorsRresponse;
import com.bestTravel.api.model.response.TicketResponse;
import com.bestTravel.infrastructure.abstract_services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Best travel - Tickets")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "servicio para crear tickets", description = "servicio para crear tickets")
    @Parameter(name = "request", description = "datos del ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "bad request",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorsRresponse.class))})
    })

    public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketRequest request){
        log.info("#### endpoint creacion de tickets");
        return ResponseEntity.ok(ticketService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "muestra un ticket por id",
            description = "muestra un ticket por id si existe")
    @Parameter(name = "id", description = "id del ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    public ResponseEntity<TicketResponse> read(@PathVariable UUID id){
        log.info("#### endpoint buscar ticket por id");
        return ResponseEntity.ok(ticketService.read(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "servicio para actualizar un ticket",
            description = "servicio para actualizar un ticket")
    @Parameters({
            @Parameter(name = "id", description = "id del ticket"),
            @Parameter(name = "request", description = "datos del ticket")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorsRresponse.class))})
    })
    public ResponseEntity<TicketResponse> update(@Valid @PathVariable UUID id,
                                                 @RequestBody TicketRequest request){
        log.info("#### endpoint actualizar ticket");
        return ResponseEntity.ok(ticketService.update(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "servicio que elimina un ticket por id",
            description = "servicio que elimina un ticket por id si existe")
    @Parameter(name = "id", description = "id del ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        log.info("#### endpoint eliminar ticket por id");
        ticketService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/price/{idFly}")
    @Operation(summary = "servicio que muestra el precio de un vuelo por su id",
            description = "servicio que muestra el precio de un vuelo por su id")
    @Parameter(name = "idFly", description = "id del vuelo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    public ResponseEntity<Map<String, BigDecimal>> viewPrice(@PathVariable Long idFly){
        log.info("#### endpoint consultar precio ticket");
        return ResponseEntity.ok(Collections.singletonMap("Precio",ticketService.findPrice(idFly)));
    }
}
