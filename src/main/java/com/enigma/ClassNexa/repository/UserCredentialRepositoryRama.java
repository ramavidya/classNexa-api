package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepositoryRama extends JpaRepository<UserCredential, String> {
}
