package com.enigma.ClassNexa.dto.Response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    @NotBlank
    private String status;

    @NotBlank
    private String message;

    @NotBlank
    private PagingResponse pagingResponse;

    @NotBlank
    private T data;

}
