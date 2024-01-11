package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.ScheduleRama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepositoryRama extends JpaRepository<ScheduleRama, String> {
}
