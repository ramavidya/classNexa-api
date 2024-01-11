package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_trainer")
@Entity
@Builder
public class TrainerRama {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String gender;

    @OneToOne
    private UserCredentialRama userCredentialRama;
}
