package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;



@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_classes")
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;




}


