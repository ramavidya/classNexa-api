package com.enigma.ClassNexa.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordRequest {
    private String id;
    private String password;
    private String new_password;
}
