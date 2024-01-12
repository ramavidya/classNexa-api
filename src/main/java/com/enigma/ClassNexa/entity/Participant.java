package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String gender;

    @OneToOne
    @JoinColumn(name = "user_credential_id", referencedColumnName = "id")
    private UserCredential userCredential;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.PERSIST)
    private List<Questions> questions;

}

