package com.enigma.ClassNexa.model.request;

import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDocumentationRequest {
    private String id;
    private String file_name;
    private String trainer;
    private String schedule;
}
