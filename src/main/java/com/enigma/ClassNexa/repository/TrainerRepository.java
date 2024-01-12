package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, String>, JpaSpecificationExecutor<Trainer> {
    Optional<Trainer> findByUserCredential(UserCredential userCredential);
}
