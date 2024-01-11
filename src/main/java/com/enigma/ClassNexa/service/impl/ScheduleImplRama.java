package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.ScheduleRama;
import com.enigma.ClassNexa.repository.ScheduleRepositoryRama;
import com.enigma.ClassNexa.service.ScheduleServiceRama;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleImplRama implements ScheduleServiceRama {
    private final ScheduleRepositoryRama scheduleRepositoryRama;
    @Override
    public ScheduleRama getByIdSchedule(String id) {
        Optional<ScheduleRama> byId = scheduleRepositoryRama.findById(id);
        ScheduleRama scheduleRama = ScheduleRama.builder()
                .id(byId.get().getId())
                .meetingLink(byId.get().getMeetingLink())
                .startClass(byId.get().getStartClass())
                .endClass(byId.get().getEndClass())
                .ClassesRama(byId.get().getClassesRama())
                .build();
        return scheduleRama;

    }
}
