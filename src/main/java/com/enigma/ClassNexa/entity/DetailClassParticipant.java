package com.enigma.ClassNexa.entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_detail_classes")
public class DetailClassParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    private Participant  participant;

    @ManyToOne
    private Classes classes;

}
