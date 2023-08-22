package com.bestTravel.api.controller;

import com.bestTravel.infrastructure.abstract_services.ModifyUserService;
import com.bestTravel.util.enums.RoleName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Best Travel - Usuarios")
public class AppUserController {
    private final ModifyUserService modifyUserService;

    @PatchMapping("/enabled/{username}")
    @Operation(summary = "servicio para habilitar/desabilitar usuarios",
            description = "ervicio para habilitar/desabilitar usuarios")
    @Parameter(name = "username", description = "alias del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    public ResponseEntity<Map<String, Boolean>> setEnabled(@PathVariable String username){
        log.info("#### endpoint para habilitar/desabilitar usuario");
        return ResponseEntity.ok(modifyUserService.enabled(username));
    }

    @PatchMapping("/add-role/{username}/{roleName}")
    @Operation(summary = "servicio para agregar rol a un usuario",
            description = "servicio para  agregar rol a un usuario")
    @Parameters({
            @Parameter(name = "username", description = "alias del usuario"),
            @Parameter(name = "roleName", description = "rol para agrgegar")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    public ResponseEntity<Map<String, List<String>>> addRole(@PathVariable String username,
                                                             @PathVariable RoleName roleName){
        log.info("#### endpoint par agregar rol a un usuario");
        return ResponseEntity.ok(modifyUserService.addRole(username, roleName));
    }

    @PatchMapping("/remove-role/{username}/{roleName}")
    @Operation(summary = "servicio para remover rol a un usuario",
            description = "servicio para  remover rol a un usuario")
    @Parameters({
            @Parameter(name = "username", description = "alias del usuario"),
            @Parameter(name = "roleName", description = "rol para agrgegar")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    public ResponseEntity<Map<String, List<String>>> aremoveRole(@PathVariable String username,
                                                                @PathVariable RoleName roleName){
        log.info("#### endpoint par remover rol a un usuario");
        return ResponseEntity.ok(modifyUserService.removeRole(username, roleName));
    }
}
