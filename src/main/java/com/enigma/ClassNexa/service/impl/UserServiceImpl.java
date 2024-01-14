package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.UserUpdateRequest;
import com.enigma.ClassNexa.repository.UserCredentialRepository;
import com.enigma.ClassNexa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCredential loadUserById(String userId) {
        return userCredentialRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"unathorized"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(UserCredential userCredential) {
        userCredentialRepository.delete(userCredential);
        return "OK";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(UserUpdateRequest request) {
        UserCredential loadUserCredential = (UserCredential) loadUserByUsername(request.getEmail());

        boolean matchesPassword = passwordEncoder.matches(request.getPassword(), loadUserCredential.getPassword());
        if (matchesPassword == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"invalid password");
        }

        userCredentialRepository.save(
                UserCredential.builder()
                        .id(loadUserCredential.getId())
                        .email(loadUserCredential.getEmail())
                        .password(passwordEncoder.encode(request.getNew_password()))
                        .roles(loadUserCredential.getRoles())
                        .build()
        );
        return "ok";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCredential getByEmail(String email) {
        return userCredentialRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"unathorized"));
    }
}
