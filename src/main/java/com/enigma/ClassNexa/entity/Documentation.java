package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "t_documetation")
public class Documentation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "file_name")
    private String fileName;
    @ManyToOne
    private Trainer trainer;
    @ManyToOne
    private Schedule schedule;

}
