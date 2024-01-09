package com.enigma.ClassNexa.model.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private PagingResponse pagingResponse;

    @NotBlank
    private T data;

}
