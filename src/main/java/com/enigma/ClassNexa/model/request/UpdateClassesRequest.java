package com.enigma.ClassNexa.model.request;

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
    private List<Participant> participants;
}
