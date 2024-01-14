package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.UserUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserCredential loadUserById(String userId);
    String delete(UserCredential userCredential);
    String update(UserUpdateRequest request);

    UserCredential getByEmail(String email);
}
