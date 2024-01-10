package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.model.request.ScheduleRequest;
import com.enigma.ClassNexa.model.response.ScheduleResponse;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.ClassesServiceBambang;
import com.enigma.ClassNexa.service.ScheduleService;
import com.enigma.ClassNexa.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClassesServiceBambang classCNService;
    private final TrainerService trainerService;

    private ScheduleResponse toScheduleResponse(Schedule schedule){
        Classes byIdClass = classCNService.getById(schedule.getClasses_id().getId());
        Trainer byIdTrainer = trainerService.getByTrainerId(byIdClass.getTrainer().getId());
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .meeting_link(schedule.getMeeting_link())
                .start_class(schedule.getStart_class())
                .end_class(schedule.getEnd_class())
                .classes_id(schedule.getClasses_id().getId())
                .classes_name(byIdClass.getName())
                .trainer(byIdTrainer.getName())
                .build();
    }

    @Override
    public ScheduleResponse create(ScheduleRequest request) {
        Classes byIdClass = classCNService.getById(request.getClasses_id());
        Schedule schedule = Schedule.builder()
                .meeting_link(request.getMeeting_link())
                .start_class(request.getStart_class())
                .end_class(request.getEnd_class())
                .classes_id(byIdClass)
                .build();
        Schedule save = scheduleRepository.save(schedule);
        return toScheduleResponse(save);
    }

    @Override
    public List<ScheduleResponse> getAll() {
        List<Schedule> all = scheduleRepository.findAll();
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        for (int i = 0; i<all.size();i++){
            Classes byIdClass = classCNService.getById(all.get(i).getClasses_id().getId());
            Trainer byIdTrainer = trainerService.getByTrainerId(byIdClass.getTrainer().getId());
            ScheduleResponse scheduleResponse =ScheduleResponse.builder()
                    .id(all.get(i).getId())
                    .meeting_link(all.get(i).getMeeting_link())
                    .start_class(all.get(i).getStart_class())
                    .end_class(all.get(i).getEnd_class())
                    .classes_id(all.get(i).getClasses_id().getId())
                    .classes_name(byIdClass.getName())
                    .trainer(byIdTrainer.getName())
                    .build();
            scheduleResponses.add(scheduleResponse);

        }
        return scheduleResponses;
    }

    @Override
    public String delete(String id) {
        scheduleRepository.deleteById(id);
        return "ok";
    }

    @Override
    public ScheduleResponse update(ScheduleRequest request) {

        Optional<Schedule> byIdSchedule = scheduleRepository.findById(request.getId());
        Classes byIdClass = classCNService.getById(request.getClasses_id());
        Schedule schedule = Schedule.builder()
                .id(byIdSchedule.get().getId())
                .meeting_link(request.getMeeting_link())
                .start_class(request.getStart_class())
                .end_class(request.getEnd_class())
                .classes_id(byIdClass)
                .build();

        Schedule save = scheduleRepository.save(schedule);

        return toScheduleResponse(save);
    }

    @Override
    public ScheduleResponse getById(String id) {
        if (id.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID schedule not found");

        Optional<Schedule> byId = scheduleRepository.findById(id);
        Classes byIdClass = classCNService.getById(byId.get().getClasses_id().getId());
        Trainer byIdTrainer = trainerService.getByTrainerId(byIdClass.getTrainer().getId());
        ScheduleResponse scheduleResponse =ScheduleResponse.builder()
                .id(byId.get().getId())
                .meeting_link(byId.get().getMeeting_link())
                .start_class(byId.get().getStart_class())
                .end_class(byId.get().getEnd_class())
                .classes_id(byId.get().getClasses_id().getId())
                .classes_name(byIdClass.getName())
                .trainer(byIdTrainer.getName())
                .build();
        return scheduleResponse;
    }

    @Override
    public Schedule getByIdSchedule(String id) {
        Optional<Schedule> byId = scheduleRepository.findById(id);
        Schedule schedule = Schedule.builder()
                .id(byId.get().getId())
                .meeting_link(byId.get().getMeeting_link())
                .start_class(byId.get().getStart_class())
                .end_class(byId.get().getEnd_class())
                .classes_id(byId.get().getClasses_id())
                .build();
        return schedule;
    }

}
