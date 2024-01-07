package com.enigma.ClassNexa.model.request;

import com.enigma.ClassNexa.entity.UserCredential;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private String gender;
    private UserCredential userCredential;
}
