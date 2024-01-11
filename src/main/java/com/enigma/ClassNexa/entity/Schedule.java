package com.enigma.ClassNexa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "t_schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String meeting_link;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date start_class;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date end_class;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private Classes classes_id;

}

