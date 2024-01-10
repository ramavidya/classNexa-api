package com.enigma.ClassNexa.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRequest {
    private String id;
    private String meeting_link;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date start_class;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date end_class;
    private String classes_id;
}
