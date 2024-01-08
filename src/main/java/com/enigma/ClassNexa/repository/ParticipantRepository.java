package com.enigma.ClassNexa.repository;

import com.Api.ClassNexa.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,String> {
}
