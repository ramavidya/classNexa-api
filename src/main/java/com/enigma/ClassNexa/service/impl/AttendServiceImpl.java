package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.model.request.AttendDetailRequest;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.response.AttendDetailResponse;
import com.enigma.ClassNexa.model.response.AttendResponse;
import com.enigma.ClassNexa.repository.AttendRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.AttendService;
import com.enigma.ClassNexa.service.AttendanceService;
import com.enigma.ClassNexa.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService {
    private final AttendRepository attendRepository;
    private final AttendanceService attendanceService;
    private final ParticipantService participantService;
    private final ScheduleRepository scheduleRepository;

    @Override
    public Attend getAttendById(String id) {
        Optional<Attend> optionalAttend = attendRepository.findById(id);
        optionalAttend.orElseThrow(() -> new RuntimeException("not found"));
        return null;

    }

    @Override
    public AttendResponse create(AttendRequest request) {

//        Attend attend = Attend.builder()
//                .participant()
//                .attendance()
//                .schedule()
//                .build();
//        attendRepository.saveAndFlush(attend);

        List<Attend> attends = new ArrayList<>();
        List<AttendDetailResponse> attendDetailResponses = new ArrayList<>();
        Optional<Schedule> byId = scheduleRepository.findById(request.getScheduleId());
        for (AttendDetailRequest attendDetailRequest : request.getAttendDetailRequests()) {
            Participant participant = participantService.getParticipantById(attendDetailRequest.getParticipantId());
            Attendance attendance = attendanceService.getAttendanceById(attendDetailRequest.getCategoryId());
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .ParticipantId(participant.getId())
                    .participantName(participant.getName())
                    .info(attendance.getCategory())
                    .build();
            Attend attend = Attend.builder()
                    .participant(participant)
                    .attendance(attendance)
                    .schedule(byId.get())
                    .build();

            attendDetailResponses.add(attendDetailResponse);
            attends.add(attend);
            attendRepository.save(attend);
        }
        AttendResponse attendResponse = AttendResponse.builder()
                .classStartedAt(byId.get().getStartClass())
                .attendDetailResponses(attendDetailResponses)
                .build();
        return attendResponse;
    }

    @Override
    public List<Attend> getAll() {
        return attendRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        attendRepository.deleteById(id);
    }

    @Override
    public Attend Update(Attend request) {
        Optional<Attend> optionalAttend = attendRepository.findById(request.getId());
        if (optionalAttend.isEmpty()) throw new RuntimeException("not found");

        return attendRepository.save(request) ;
    }
}
