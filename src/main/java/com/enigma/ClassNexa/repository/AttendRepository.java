package com.enigma.ClassNexa.repository;


import com.enigma.ClassNexa.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendRepository extends JpaRepository<Attend, String>, JpaSpecificationExecutor<Attend> {

}
