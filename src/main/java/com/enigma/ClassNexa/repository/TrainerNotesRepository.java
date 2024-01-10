package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.TrainerNotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerNotesRepository extends JpaRepository<TrainerNotes, String>, JpaSpecificationExecutor<TrainerNotes> {
    TrainerNotes findBySchedule_Id(String scheduleId);
    TrainerNotes findByTrainer_Id(String trainerId);
    Page<TrainerNotes> findAllByTrainerOrSchedule(Pageable pageable, String trainer, String schedule);
}
