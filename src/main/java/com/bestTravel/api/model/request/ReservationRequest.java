package com.bestTravel.api.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationRequest implements Serializable {
    @Size(min = 18, max = 20, message = "the size have to a length between 18 and 20 characters")
    @NotBlank(message = "id client is mandatory")
    private String idClient;
    @Positive
    @NotNull(message = "id hotel is mandatrory")
    private Long idHotel;
    @Min(value = 1, message = "min one days to make reservation")
    @Max(value = 30, message = "max 30 days to make reservation")
    @NotNull(message = "totalDays is mandatory")
    private Integer totalDays;
    @Email(message = "invalid email")
    private String email;
}
