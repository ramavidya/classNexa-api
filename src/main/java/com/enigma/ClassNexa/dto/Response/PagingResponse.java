package com.enigma.ClassNexa.dto.Response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PagingResponse {

    @NotBlank
    private Long totalElements;

    @NotBlank
    private int totalPages;

    @NotBlank
    private int page;

    @NotBlank
    private int size;

}
