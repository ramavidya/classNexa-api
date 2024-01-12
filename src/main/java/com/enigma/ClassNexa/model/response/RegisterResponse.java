package com.enigma.ClassNexa.model.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String email;
    private String name;
    private List<String> roles;
}
