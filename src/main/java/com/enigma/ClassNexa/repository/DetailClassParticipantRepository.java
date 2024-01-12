package com.enigma.ClassNexa.repository;


import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailClassParticipantRepository extends JpaRepository<DetailClassParticipant, String> {
    List<DetailClassParticipant> findByClassesId(String id);
    List<DetailClassParticipant> deleteByClassesId(String id);

    Optional<DetailClassParticipant> findByParticipant(Participant participant);


}
