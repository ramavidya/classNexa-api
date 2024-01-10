package com.enigma.ClassNexa.model.response;

import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private String status;
    private String message;
<<<<<<< HEAD
    private PagingResponse pagingResponse;
=======
    private PagingResponse paging;
>>>>>>> dev/putra
    private T data;
}
