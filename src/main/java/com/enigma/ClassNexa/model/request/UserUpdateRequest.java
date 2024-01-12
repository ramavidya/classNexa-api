package com.enigma.ClassNexa.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    private String email;
    private String password;
    private String new_password;

}
