package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions,String>, JpaSpecificationExecutor<Questions> {


}
