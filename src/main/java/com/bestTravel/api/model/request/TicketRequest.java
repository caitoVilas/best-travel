package com.bestTravel.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketRequest implements Serializable {
    @Size(min = 18, max = 20, message = "the size have to a length between 18 and 20 characters")
    @NotBlank(message = "id client is mandatory")
    private String idClient;
    @Positive
    @NotNull(message = "id flight is mandatory")
    private Long idFly;
}
