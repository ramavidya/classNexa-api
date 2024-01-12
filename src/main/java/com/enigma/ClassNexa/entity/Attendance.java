package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private String id;

    private String category;

}
