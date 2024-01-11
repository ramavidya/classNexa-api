package com.enigma.ClassNexa.model.request;

import com.enigma.ClassNexa.entity.Participant;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendRequest {
    private String scheduleId;
    private List<AttendDetailRequest> attendDetailRequests;


}
