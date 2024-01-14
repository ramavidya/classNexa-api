package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.TrainerNotes;
import com.enigma.ClassNexa.model.request.SearchTrainerNotesRequest;
import com.enigma.ClassNexa.model.request.TrainerNotesRequest;
import com.enigma.ClassNexa.model.response.TrainerNotesResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrainerNotesService {
    TrainerNotesResponse create(TrainerNotesRequest request);
    List<TrainerNotesResponse> getAll();
    TrainerNotesResponse getById(String id);
    TrainerNotesResponse update(TrainerNotesRequest trainerNotes);
    String delete(String id);
    Page<TrainerNotes> getAllTrainerNotes(SearchTrainerNotesRequest request);
}
