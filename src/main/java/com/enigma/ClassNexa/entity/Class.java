package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_classes")
@Entity
@Builder
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany
    private List<Participant> participant;

    @OneToMany
    private Trainer trainer;
}
