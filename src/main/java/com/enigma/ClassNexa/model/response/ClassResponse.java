package com.enigma.ClassNexa.model.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassResponse {

    @NotBlank
    private String id;

    @NotBlank
    private String classes;

    @NotBlank
    private String trainer;

    @NotBlank
    private List<String> participant;

}
