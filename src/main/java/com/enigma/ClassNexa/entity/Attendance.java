package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_attendance")
@Entity
@Builder
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String category;
}
