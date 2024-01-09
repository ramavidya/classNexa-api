package com.enigma.ClassNexa.model.response;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.entity.Participant;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendResponse {

    private Timestamp classStartedAt;

    private List<AttendDetailResponse> attendDetailResponses;
}
