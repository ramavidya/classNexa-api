package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @Column(name = "absent_reasons")
    private String absentReasons;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
