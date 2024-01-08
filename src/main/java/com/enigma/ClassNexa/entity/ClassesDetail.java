package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_classes_detail")
@Entity
@Builder
public class ClassesDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToMany
    private List<Participant> participants;

    @ManyToOne
    private Trainer trainer;
}
