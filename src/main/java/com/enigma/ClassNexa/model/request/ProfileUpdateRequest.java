package com.enigma.ClassNexa.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUpdateRequest {
    private String id;
    private String name;
    private String address;
    private String gender;
    private String phoneNumber;
}
