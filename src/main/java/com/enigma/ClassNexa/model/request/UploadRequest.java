package com.enigma.ClassNexa.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadRequest {

    private String email;

    private String name;

    private String gender;

    private String address;

    private String phoneNumber;

}
