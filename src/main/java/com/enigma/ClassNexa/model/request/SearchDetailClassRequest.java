package com.enigma.ClassNexa.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDetailClassRequest {

    @NotNull
    private int page;

    @NotNull
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
