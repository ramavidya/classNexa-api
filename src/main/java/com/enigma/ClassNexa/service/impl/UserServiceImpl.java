package com.enigma.ClassNexa.service.impl;

import com.enigma.classnexa.entity.UserCredential;
import com.enigma.classnexa.repository.UserCredentialRepository;
import com.enigma.classnexa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCredential loadUserById(String userId) {
        return userCredentialRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"unathorized"));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"unathorized"));
    }
}
