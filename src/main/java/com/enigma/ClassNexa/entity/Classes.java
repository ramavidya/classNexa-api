package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "m_classes")
public class Classes {
    @Id
    private String id;
    private String name;
    private String participant_id;
    @ManyToOne
    private Trainer trainer;
}
