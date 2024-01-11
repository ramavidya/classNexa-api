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
    private ParticipantRama participantRama;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleRama scheduleRama;
}
