package com.enigma.ClassNexa.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchStatusRequest {
    private Integer page;
    private Integer size;
}
