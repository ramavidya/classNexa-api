package com.enigma.ClassNexa.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PagingResponse {

    private Long totalElements;
    private int totalPages;
    private int page;
    private int size;

}
