package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.dto.request.AttendDetailRequest;
import com.enigma.ClassNexa.dto.request.AttendRequest;
import com.enigma.ClassNexa.dto.request.SearchAttendRequest;
import com.enigma.ClassNexa.dto.request.UpdateAttendRequest;
import com.enigma.ClassNexa.dto.response.AttendDetailResponse;
import com.enigma.ClassNexa.dto.response.AttendResponse;
import com.enigma.ClassNexa.dto.response.SingleAttendResponse;
import com.enigma.ClassNexa.repository.AttendRepository;
import com.enigma.ClassNexa.service.AttendService;
import com.enigma.ClassNexa.service.AttendanceService;
import com.enigma.ClassNexa.service.ParticipantService;
import com.enigma.ClassNexa.service.ScheduleService;
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
                .scheduleId(optionalAttend.get().getSchedule().getId())
                .classStartedAt(optionalAttend.get().getSchedule().getStartClass())
                .attendDetailResponse(attendDetailResponse)
                .build();
        return attendResponse;
    }

    @Override
    public AttendResponse create(AttendRequest request) {
        List<Attend> attends = new ArrayList<>();
        List<AttendDetailResponse> attendDetailResponses = new ArrayList<>();
        Schedule schedule = scheduleService.getByIdSchedule(request.getScheduleId());
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
                    .schedule(schedule)
                    .build();

            attendDetailResponses.add(attendDetailResponse);
            attends.add(attend);
            attendRepository.save(attend);
        }
        AttendResponse attendResponse = AttendResponse.builder()
                .scheduleId(schedule.getId())
                .classStartedAt(schedule.getStartClass())
                .attendDetailResponses(attendDetailResponses)
                .build();
        return attendResponse;
    }

    @Override
    public List<SingleAttendResponse> getAll(SearchAttendRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        log.info(request.getParticipantId());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<Attend> specification = getProductSpecification(request);
        Page<Attend> all = attendRepository.findAll(specification, pageable);
        List<SingleAttendResponse> attendResponses = new ArrayList<>();
        for (int i = 0; i < all.getContent().size(); i++){
            Participant participant = participantService.getParticipantById(all.getContent().get(i).getParticipant().getId());
            Attendance attendance = attendanceService.getAttendanceById(all.getContent().get(i).getAttendance().getId());
            Schedule schedule = scheduleService.getByIdSchedule(all.getContent().get(i).getSchedule().getId());
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .ParticipantId(participant.getId())
                    .participantName(participant.getName())
                    .info(attendance.getCategory())
                    .build();
            SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                    .id(all.getContent().get(i).getId())
                    .scheduleId(schedule.getId())
                    .classStartedAt(schedule.getStartClass())
                    .attendDetailResponse(attendDetailResponse)
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
        Schedule schedule = scheduleService.getByIdSchedule(request.getScheduleId());
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
                .scheduleId(schedule.getId())
                .classStartedAt(schedule.getStartClass())
                .attendDetailResponse(attendDetailResponse)
                .build();
        attend = Attend.builder()
                .id(request.getId())
                .schedule(schedule)
                .participant(participantById)
                .attendance(attendance)
                .build();
         attendRepository.save(attend);
         return attendResponse;
    }
    private Specification<Attend> getProductSpecification(SearchAttendRequest request) {
        Specification<Attend> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getParticipantId() != null){
                Predicate namePredicate = criteriaBuilder.like(root.get("participant").get("id"), "%" + request.getParticipantId() + "%");
                predicates.add(namePredicate);
            }
            if (request.getScheduleId() != null){
                Predicate minPricePredicate = criteriaBuilder.equal(root.get("schedule").get("id"), request.getScheduleId());
                predicates.add(minPricePredicate);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });
        return specification;
    }
}
