package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, String>, JpaSpecificationExecutor<Classes> {
}
