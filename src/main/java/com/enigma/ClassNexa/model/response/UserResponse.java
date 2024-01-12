package com.enigma.ClassNexa.model.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String gender;
    private String address;
    private String email;
    private String phoneNumber;
}
