package com.enigma.ClassNexa.model.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetNumberRequest {

    private List<String> number;

}
