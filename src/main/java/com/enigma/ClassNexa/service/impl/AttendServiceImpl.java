package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.entity.ParticipantRama;
import com.enigma.ClassNexa.entity.ScheduleRama;
import com.enigma.ClassNexa.model.request.AttendDetailRequest;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.request.SearchAttendRequest;
import com.enigma.ClassNexa.model.request.UpdateAttendRequest;
import com.enigma.ClassNexa.model.response.AttendDetailResponse;
import com.enigma.ClassNexa.model.response.AttendResponse;
import com.enigma.ClassNexa.model.response.SingleAttendResponse;
import com.enigma.ClassNexa.repository.AttendRepository;
import com.enigma.ClassNexa.service.AttendService;
import com.enigma.ClassNexa.service.AttendanceService;
import com.enigma.ClassNexa.service.ParticipantServiceRama;
import com.enigma.ClassNexa.service.ScheduleServiceRama;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService {
    private final AttendRepository attendRepository;
    private final AttendanceService attendanceService;
    private final ParticipantServiceRama participantServiceRama;
    private final ScheduleServiceRama scheduleServiceRama;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SingleAttendResponse getAttendById(String id) {
        Optional<Attend> optionalAttend = attendRepository.findById(id);
        optionalAttend.orElseThrow(() -> new RuntimeException("not found"));
        AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                .ParticipantId(optionalAttend.get().getParticipantRama().getId())
                .participantName(optionalAttend.get().getParticipantRama().getName())
                .info(optionalAttend.get().getAttendance().getCategory())
                .build();
         SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                .id(optionalAttend.get().getId())
                .scheduleId(optionalAttend.get().getScheduleRama().getId())
                .classStartedAt(optionalAttend.get().getScheduleRama().getStartClass())
                .attendDetailResponse(attendDetailResponse)
                .build();
        return attendResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendResponse create(AttendRequest request) {
        List<Attend> attends = new ArrayList<>();
        List<AttendDetailResponse> attendDetailResponses = new ArrayList<>();
        ScheduleRama scheduleRama = scheduleServiceRama.getByIdSchedule(request.getScheduleId());
        for (AttendDetailRequest attendDetailRequest : request.getAttendDetailRequests()) {
            ParticipantRama participantRama = participantServiceRama.getByParticipantId(attendDetailRequest.getParticipantId());
            Attendance attendance = attendanceService.getAttendanceById(attendDetailRequest.getCategoryId());
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .ParticipantId(participantRama.getId())
                    .participantName(participantRama.getName())
                    .info(attendance.getCategory())
                    .build();
            Attend attend = Attend.builder()
                    .participantRama(participantRama)
                    .attendance(attendance)
                    .scheduleRama(scheduleRama)
                    .build();

            attendDetailResponses.add(attendDetailResponse);
            attends.add(attend);
            attendRepository.save(attend);
        }
        AttendResponse attendResponse = AttendResponse.builder()
                .scheduleId(scheduleRama.getId())
                .classStartedAt(scheduleRama.getStartClass())
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
            ParticipantRama participantRama = participantServiceRama.getByParticipantId(all.getContent().get(i).getParticipantRama().getId());
            Attendance attendance = attendanceService.getAttendanceById(all.getContent().get(i).getAttendance().getId());
            ScheduleRama scheduleRama = scheduleServiceRama.getByIdSchedule(all.getContent().get(i).getScheduleRama().getId());
            AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                    .ParticipantId(participantRama.getId())
                    .participantName(participantRama.getName())
                    .info(attendance.getCategory())
                    .build();
            SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                    .id(all.getContent().get(i).getId())
                    .scheduleId(scheduleRama.getId())
                    .classStartedAt(scheduleRama.getStartClass())
                    .attendDetailResponse(attendDetailResponse)
                    .build();
            attendResponses.add(attendResponse);
        }
        return attendResponses;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        attendRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SingleAttendResponse Update(UpdateAttendRequest request) {
        Optional<Attend> optionalAttend = attendRepository.findById(request.getId());
        if (optionalAttend.isEmpty()) throw new RuntimeException("not found");
        ScheduleRama scheduleRama = scheduleServiceRama.getByIdSchedule(request.getScheduleId());
        ParticipantRama participantRamaById = participantServiceRama.getByParticipantId(request.getParticipantId());
        Attendance attendance = attendanceService.getAttendanceById(request.getCategoryId());
        Attend attend = optionalAttend.get();
        AttendDetailResponse attendDetailResponse = AttendDetailResponse.builder()
                .ParticipantId(participantRamaById.getId())
                .participantName(attend.getParticipantRama().getName())
                .info(attendance.getCategory())
                .build();
        SingleAttendResponse attendResponse = SingleAttendResponse.builder()
                .id(optionalAttend.get().getId())
                .scheduleId(scheduleRama.getId())
                .classStartedAt(scheduleRama.getStartClass())
                .attendDetailResponse(attendDetailResponse)
                .build();
        attend = Attend.builder()
                .id(request.getId())
                .scheduleRama(scheduleRama)
                .participantRama(participantRamaById)
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
