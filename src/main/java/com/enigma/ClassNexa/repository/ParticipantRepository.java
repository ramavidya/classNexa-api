package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByUserCredential(UserCredential userCredential);
}

