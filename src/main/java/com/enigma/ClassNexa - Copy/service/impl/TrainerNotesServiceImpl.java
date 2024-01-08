package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.TrainerNotes;
import com.enigma.ClassNexa.model.TrainerNotesRequest;
import com.enigma.ClassNexa.repository.TrainerNotesRepository;
import com.enigma.ClassNexa.service.ScheduleService;
import com.enigma.ClassNexa.service.TrainerNotesService;
import com.enigma.ClassNexa.service.TrainerServicebamss;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerNotesServiceImpl implements TrainerNotesService {
    private final TrainerNotesRepository trainerNotesRepository;
    private final TrainerServicebamss trainerService;
    private final ScheduleService scheduleService;

    @Override
    public TrainerNotes create(TrainerNotesRequest request) {
        Trainer byIdTrainer = trainerService.getById(request.getTrainer());
        Schedule byIdSchedule = scheduleService.getByIdSchedule(request.getSchedule());

        TrainerNotes trainerNotes =TrainerNotes.builder()
                .notes(request.getNotes())
                .trainer(byIdTrainer)
                .schedule(byIdSchedule)
                .build();
        TrainerNotes save = trainerNotesRepository.save(trainerNotes);
        return save;
    }

    @Override
    public List<TrainerNotes> getAll() {
        List<TrainerNotes> all = trainerNotesRepository.findAll();
        List<TrainerNotes> trainerNotes = new ArrayList<>();
        for (int i=0;i< all.size();i++){
            TrainerNotes notes = TrainerNotes.builder()
                    .id(all.get(i).getId())
                    .notes(all.get(i).getNotes())
                    .trainer(all.get(i).getTrainer())
                    .schedule(all.get(i).getSchedule())
                    .build();
            trainerNotes.add(notes);
        }
        return trainerNotes;
    }

    @Override
    public TrainerNotes getById(String id) {
        Optional<TrainerNotes> byId = trainerNotesRepository.findById(id);
        TrainerNotes trainerNotes = TrainerNotes.builder()
                .id(byId.get().getId())
                .notes(byId.get().getNotes())
                .trainer(byId.get().getTrainer())
                .schedule(byId.get().getSchedule())
                .build();
        return trainerNotes;
    }

    @Override
    public TrainerNotes update(TrainerNotesRequest trainerNotes) {
        Optional<TrainerNotes> byId = trainerNotesRepository.findById(trainerNotes.getId());
        Trainer byId1 = trainerService.getById(trainerNotes.getTrainer());
        Schedule byIdSchedule = scheduleService.getByIdSchedule(trainerNotes.getSchedule());

        TrainerNotes notes = TrainerNotes.builder()
                .id(byId.get().getId())
                .notes(trainerNotes.getNotes())
                .trainer(byId1)
                .schedule(byIdSchedule)
                .build();

        return trainerNotesRepository.save(notes);
    }

    @Override
    public String delete(String id) {
        trainerNotesRepository.deleteById(id);
        return "ok";
    }


}
