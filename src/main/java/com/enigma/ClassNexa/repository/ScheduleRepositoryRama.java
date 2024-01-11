package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepositoryRama extends JpaRepository<Schedule, String> {
}
