package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "t_questions")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "question")
    private String question;

    @Column(name = "course")
    private String course;

    @Column(name = "chapter")
    private String chapter;

    @ManyToOne
    @JoinColumn(name = "questions_status_id")
    private Questions_Status questions_status;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;


}
