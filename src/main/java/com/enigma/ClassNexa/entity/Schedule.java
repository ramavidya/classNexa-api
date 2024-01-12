package com.enigma.ClassNexa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_schedule")
@Entity
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String meeting_link;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date start_class;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date end_class;

//    @ManyToOne
//    @JoinColumn(name = "classes_id")
//    private Classes classes_id;

}

