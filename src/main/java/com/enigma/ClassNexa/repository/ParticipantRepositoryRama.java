package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.ParticipantRama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepositoryRama extends JpaRepository<ParticipantRama, String> {
    List<ParticipantRama> findByNameIn(List<String> name);
}
