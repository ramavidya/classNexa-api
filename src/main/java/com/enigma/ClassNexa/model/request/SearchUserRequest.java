package com.enigma.ClassNexa.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserRequest {

    private String name;
    private Integer page;
    private Integer size;

}
