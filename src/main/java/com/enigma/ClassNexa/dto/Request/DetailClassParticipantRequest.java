package com.enigma.ClassNexa.dto.Request;

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
