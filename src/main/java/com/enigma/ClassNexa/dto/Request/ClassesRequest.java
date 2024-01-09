package com.enigma.ClassNexa.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassesRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String trainerId;

    @NotBlank
    private List<DetailClassParticipantRequest> participants;

}
