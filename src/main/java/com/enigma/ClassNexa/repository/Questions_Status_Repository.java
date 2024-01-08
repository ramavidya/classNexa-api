package com.enigma.ClassNexa.repository;


import com.enigma.ClassNexa.entity.Questions_Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Questions_Status_Repository extends JpaRepository<Questions_Status,String> {
}
