package com.enigma.ClassNexa.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
