package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.TrainerNotes;
import com.enigma.ClassNexa.model.TrainerNotesRequest;

import java.util.List;

public interface TrainerNotesService {
    TrainerNotes create(TrainerNotesRequest request);
    List<TrainerNotes> getAll();
    TrainerNotes getById(String id);
    TrainerNotes update(TrainerNotesRequest trainerNotes);
    String delete(String id);
}
