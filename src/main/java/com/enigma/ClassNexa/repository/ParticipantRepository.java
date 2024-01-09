package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {
    List<Participant> findByNameIn(List<String> name);
}
