package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.model.request.AttendDetailRequest;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.request.SearchAttendRequest;
import com.enigma.ClassNexa.model.request.UpdateAttendRequest;
import com.enigma.ClassNexa.model.response.AttendDetailResponse;
import com.enigma.ClassNexa.model.response.AttendResponse;
import com.enigma.ClassNexa.model.response.SingleAttendResponse;
import com.enigma.ClassNexa.repository.AttendRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.AttendService;
import com.enigma.ClassNexa.service.AttendanceService;
import com.enigma.ClassNexa.service.ParticipantService;
import com.enigma.ClassNexa.service.ScheduleService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService {
    private final AttendRepository attendRepository;
    private final AttendanceService attendanceService;
    private final ParticipantService participantService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;

    @Override
    public SingleAttendResponse getAttendById(String id) {
        Optional<Attend> optionalAttend = attendRepository.findById(id);
        optionalAttend.orElseThrow(() -> new RuntimeException("not found"));
        AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                .ParticipantId(optionalAttend.get().getParticipant().getId())
                .participantName(optionalAttend.get().getParticipant().getName())
                .info(optionalAttend.get().getAttendance().getCategory())
                .build();
        SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                .id(optionalAttend.get().getId())
                .classStartedAt(optionalAttend.get().getSchedule().getStartClass())
                .attendDetailResponse(attendDetailResponse)
                .build();
        return attendResponse;
    }

    @Override
    public AttendResponse create(AttendRequest request) {
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
    public List<SingleAttendResponse> getAllWithoutFilter(SearchAttendRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        log.info(request.getParticipantName());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        //Specification<Attend> specification = getAttendSpesification(request);
        List<Attend> all = attendRepository.findAll();
        List<AttendDetailResponse> attendDetailResponses = new ArrayList<>();
        List<SingleAttendResponse> attendResponses = new ArrayList<>();
        for (int i = 0; i < all.size(); i++){
            Participant participant = participantService.getParticipantById(all.get(i).getParticipant().getId());
            Attendance attendance = attendanceService.getAttendanceById(all.get(i).getAttendance().getId());
            Schedule schedule = scheduleService.getByIdSchedule(all.get(i).getSchedule().getId());

            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .ParticipantId(participant.getId())
                    .participantName(participant.getName())
                    .info(attendance.getCategory())
                    .build();
            attendDetailResponses.add(attendDetailResponse);
            SingleAttendResponse singleAttendResponse = SingleAttendResponse.builder()
                    .id(all.get(i).getId())
                    .classStartedAt(schedule.getStartClass())
                    .attendDetailResponse(attendDetailResponse)
                    .build();

            attendResponses.add(singleAttendResponse);
        }
        return attendResponses;
    }
//    private Specification<Attend> getAttendSpesification(SearchAttendRequest request) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (request.getParticipantName() != null) {
//
//                Predicate name = criteriaBuilder.like(
//                        criteriaBuilder.lower(root.get("t_attend").get("participant_id").get("name")),
//                        "%" + request.getParticipantName().toLowerCase() + "%"
//                );
//                predicates.add(name);
//            }
//            if (request.getClassStartedAt() != null) {
//                Predicate startClass = criteriaBuilder.equal(root.get("schedule_id").get("start_class"), request.getClassStartedAt());
//                predicates.add(startClass);
//            }
//            return query
//                    .where(predicates.toArray(new Predicate[]{}))
//                    .getRestriction();
//        };
//    }

    @Override
    public List<AttendResponse> getAll() {
        List<Attend> all = attendRepository.findAll();List<AttendDetailResponse> attendDetailResponses = new ArrayList<>();
        List<AttendResponse> attendResponses = new ArrayList<>();
        for (int i = 0; i < all.size(); i++){
            Participant participant = participantService.getParticipantById(all.get(i).getParticipant().getId());
            Attendance attendance = attendanceService.getAttendanceById(all.get(i).getAttendance().getId());
            Optional<Schedule> optionalSchedule = scheduleRepository.findById(all.get(i).getSchedule().getId());
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .ParticipantId(participant.getId())
                    .participantName(participant.getName())
                    .info(attendance.getCategory())
                    .build();
            attendDetailResponses.add(attendDetailResponse);
            AttendResponse attendResponse = AttendResponse.builder()
                    .id(all.get(i).getId())
                    .classStartedAt(optionalSchedule.get().getStartClass())
                    .attendDetailResponses(attendDetailResponses)
                    .build();
            attendResponses.add(attendResponse);
        }
        return attendResponses;
    }

    @Override
    public void deleteById(String id) {
        attendRepository.deleteById(id);
    }

    @Override
    public SingleAttendResponse Update(UpdateAttendRequest request) {
        Optional<Attend> optionalAttend = attendRepository.findById(request.getId());
        if (optionalAttend.isEmpty()) throw new RuntimeException("not found");
        Optional<Schedule> byId = scheduleRepository.findById(request.getScheduleId());
        Participant participantById = participantService.getParticipantById(request.getParticipantId());
        Attendance attendance = attendanceService.getAttendanceById(request.getCategoryId());
        Attend attend = optionalAttend.get();
        AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                .ParticipantId(participantById.getId())
                .participantName(attend.getParticipant().getName())
                .info(attendance.getCategory())
                .build();
        SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                .id(optionalAttend.get().getId())
                .classStartedAt(byId.get().getStartClass())
                .attendDetailResponse(attendDetailResponse)
                .build();
        attend = Attend.builder()
                .id(request.getId())
                .schedule(byId.get())
                .participant(participantById)
                .attendance(attendance)
                .build();
         attendRepository.save(attend);
         return attendResponse;
    }

    @Override
    public List<SingleAttendResponse> getAllWithFilter(String scheduleId) {
        scheduleService.getByIdSchedule(scheduleId);
        return null;
    }
}
