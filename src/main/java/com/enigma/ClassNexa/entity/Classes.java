package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_classes")
@Entity
@Builder
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;


}
