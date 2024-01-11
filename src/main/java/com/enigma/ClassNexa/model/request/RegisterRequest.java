package com.enigma.ClassNexa.model.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
    private String address;
    private String phoneNumber;
}
