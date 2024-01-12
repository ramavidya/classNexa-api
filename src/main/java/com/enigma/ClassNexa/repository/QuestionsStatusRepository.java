package com.enigma.ClassNexa.repository;


import com.enigma.ClassNexa.entity.Questions_Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsStatusRepository extends JpaRepository<Questions_Status,String> {
}
