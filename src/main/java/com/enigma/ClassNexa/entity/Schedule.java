package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "t_schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "meeting_link")
    private String meeting_link;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp start_class;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp end_class;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private Classes classes;
}
