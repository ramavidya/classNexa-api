package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "m_trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String address;
    private String phone_number;
    private String gender;
    @ManyToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential user_credential;
}
