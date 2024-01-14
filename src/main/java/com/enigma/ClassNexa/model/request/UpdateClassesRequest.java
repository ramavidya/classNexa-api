package com.enigma.ClassNexa.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import java.util.List;


@Builder

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClassesRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String trainerId;

    @NotNull
    private List<DetailClassParticipantRequest> participants;

}
