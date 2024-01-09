package com.enigma.ClassNexa.model.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PagingResponse {

    @NotNull
    private Long totalElements;

    @NotNull
    private int totalPages;

    @NotNull
    private int page;

    @NotNull
    private int size;

}
