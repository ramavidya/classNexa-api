package com.enigma.ClassNexa.model;

import com.enigma.ClassNexa.entity.UserCredential;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerClaim {
    private String id;

    private String name;

    private String address;

    private String phoneNumber;

    private String gender;

    private UserCredential userCredential;
}
