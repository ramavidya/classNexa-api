package com.enigma.ClassNexa.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantNameResponse {

    private List<String> participantName;
}
