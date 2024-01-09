package com.enigma.ClassNexa.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDetailClassRequest {

    @NotBlank
    private int page;

    @NotBlank
    private int size;

    @NotBlank
    private String direction;

    @NotBlank
    private String sortBy;

    @NotBlank
    private String classes;

    @NotBlank
    private String trainer;

}
