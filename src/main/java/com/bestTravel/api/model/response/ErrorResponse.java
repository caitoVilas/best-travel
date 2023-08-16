package com.bestTravel.api.model.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author claudio.vilas
 * date: 08/2023
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends BaseErrorResponse{
    private String message;
}
