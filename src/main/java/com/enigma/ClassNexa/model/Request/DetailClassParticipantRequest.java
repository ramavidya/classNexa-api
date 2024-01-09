package com.enigma.ClassNexa.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailClassParticipantRequest {

    @NotBlank
    private String id;

}
