package com.enigma.ClassNexa.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTrainerNotesRequest {
    private Integer page;
    private Integer size;
    private String schedule;
    private String classes;
}
