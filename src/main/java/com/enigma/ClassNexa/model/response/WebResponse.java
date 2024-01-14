package com.enigma.ClassNexa.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private String status;
    private String message;
    private PagingResponse pagingResponse;
    private T data;
}

