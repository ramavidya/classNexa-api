package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.model.request.AttendDetailRequest;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.request.SearchAttendRequest;
import com.enigma.ClassNexa.model.request.UpdateAttendRequest;
import com.enigma.ClassNexa.model.response.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    @Transactional(rollbackFor = Exception.class)
    public SingleAttendResponse getAttendById(String id) {
        Optional<Attend> optionalAttend = attendRepository.findById(id);
        optionalAttend.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
        AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                .id(optionalAttend.get().getId())
                .ParticipantId(optionalAttend.get().getParticipant().getId())
                .participantName(optionalAttend.get().getParticipant().getName())
                .info(optionalAttend.get().getAttendance().getCategory())
                .absentReasons(optionalAttend.get().getAbsentReasons())
                .build();
         SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                .scheduleId(optionalAttend.get().getSchedule().getId())
                .classStartedAt(optionalAttend.get().getSchedule().getStart_class())
                .attendDetailResponse(attendDetailResponse)
                .build();
        return attendResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendResponse create(AttendRequest request) {
        List<Attend> attends = new ArrayList<>();
        List<AttendDetailResponse> attendDetailResponses = new ArrayList<>();
        Schedule schedule = scheduleService.getByIdSchedule(request.getScheduleId());
        for (AttendDetailRequest attendDetailRequest : request.getAttendDetailRequests()) {
            Participant participant = participantService.getByParticipantId(attendDetailRequest.getParticipantId());
            Attendance attendance = attendanceService.getAttendanceById(attendDetailRequest.getCategoryId());
            Attend attend = Attend.builder()
                    .participant(participant)
                    .attendance(attendance)
                    .schedule(schedule)
                    .absentReasons(attendDetailRequest.getAbsentReasons())
                    .build();
            attendRepository.saveAndFlush(attend);
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .id(attend.getId())
                    .ParticipantId(participant.getId())
                    .participantName(participant.getName())
                    .info(attendance.getCategory())
                    .absentReasons(attend.getAbsentReasons())
                    .build();
            attendDetailResponses.add(attendDetailResponse);
            attends.add(attend);
        }
        AttendResponse attendResponse = AttendResponse.builder()
                .scheduleId(schedule.getId())
                .classStartedAt(schedule.getStart_class())
                .attendDetailResponses(attendDetailResponses)
                .build();
        return attendResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SingleAttendResponse> getAll(SearchAttendRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        log.info(request.getParticipantId());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<Attend> specification = getProductSpecification(request);
        Page<Attend> all = attendRepository.findAll(specification, pageable);
        List<SingleAttendResponse> attendResponses = new ArrayList<>();
        for (int i = 0; i < all.getContent().size(); i++){
            Participant participant = participantService.getByParticipantId(all.getContent().get(i).getParticipant().getId());
            Attendance attendance = attendanceService.getAttendanceById(all.getContent().get(i).getAttendance().getId());
            Schedule schedule = scheduleService.getByIdSchedule(all.getContent().get(i).getSchedule().getId());
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .id(all.getContent().get(i).getId())
                    .ParticipantId(participant.getId())
                    .participantName(participant.getName())
                    .info(attendance.getCategory())
                    .absentReasons(all.getContent().get(i).getAbsentReasons())
                    .build();
            SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                    .scheduleId(schedule.getId())
                    .classStartedAt(schedule.getStart_class())
                    .attendDetailResponse(attendDetailResponse)
                    .build();
            attendResponses.add(attendResponse);
        }
        return attendResponses;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        if (attendRepository.findById(id).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        attendRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SingleAttendResponse Update(UpdateAttendRequest request) {
        Optional<Attend> optionalAttend = attendRepository.findById(request.getId());
        if (optionalAttend.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        Schedule schedule = scheduleService.getByIdSchedule(request.getScheduleId());
        Participant participantById = participantService.getByParticipantId(request.getParticipantId());
        Attendance attendance = attendanceService.getAttendanceById(request.getCategoryId());
        Attend attend = optionalAttend.get();
        attend = Attend.builder()
                .id(request.getId())
                .schedule(schedule)
                .participant(participantById)
                .attendance(attendance)
                .absentReasons(request.getAbsentReasons())
                .build();
        AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                .id(attend.getId())
                .ParticipantId(participantById.getId())
                .participantName(attend.getParticipant().getName())
                .info(attendance.getCategory())
                .absentReasons(attend.getAbsentReasons())
                .build();
        SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                .scheduleId(schedule.getId())
                .classStartedAt(schedule.getStart_class())
                .attendDetailResponse(attendDetailResponse)
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
