package com.enigma.ClassNexa.modul.request;

import com.enigma.ClassNexa.entity.Participant;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateClassesRequest {
    private String classesId;
    private String name;
    private List<Participant> participantRamas;
}
