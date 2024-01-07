package com.enigma.ClassNexa.service;

import com.enigma.classnexa.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserCredential loadUserById(String userId);

}
