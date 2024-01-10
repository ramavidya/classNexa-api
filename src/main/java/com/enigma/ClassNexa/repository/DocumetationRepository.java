package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumetationRepository extends JpaRepository<Documentation, String> {
    Documentation findByFileName(String fileName);
}
