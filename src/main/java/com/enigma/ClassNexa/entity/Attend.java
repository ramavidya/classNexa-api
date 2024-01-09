package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_attend")
@Entity
@Builder
public class Attend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Attendance attendance;

    @ManyToOne
    private Schedule schedule;
}
