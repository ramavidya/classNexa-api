package com.enigma.ClassNexa.repository;

import classnexa.ClassNexa.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendRepository extends JpaRepository<Attend, String> {
}
