package com.enigma.ClassNexa.model;

import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentationClaim {
    private String id;
    private String fileName;
    private String trainer;
    private String schedule;
    private String path;
}
