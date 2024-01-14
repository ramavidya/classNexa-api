package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.*;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.repository.ParticipantRepository;
import com.enigma.ClassNexa.service.ClassDetailService;
import com.enigma.ClassNexa.service.ParticipantService;
import com.enigma.ClassNexa.service.RestTemplateService;
import com.enigma.ClassNexa.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserService userService;
    private final RestTemplateService restTemplateService;
    private final ClassDetailService classDetailService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(UserCreateRequest request) throws IOException {

        Participant buildParticipant = Participant.builder()
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(request.getUserCredential())
                .build();

        Participant participant = participantRepository.saveAndFlush(buildParticipant);

        if (request.getPhoneNumber() != null) {
            TargetNumberRequest buildTargetNumber = TargetNumberRequest.builder()
                    .number(List.of(participant.getPhoneNumber()))
                    .build();
            restTemplateService.sendMessageRegisterWhatsapp(buildTargetNumber);
        }
        return participant.getName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createList(List<Participant> participants) throws IOException {
        List<Participant> participantsList = participantRepository.saveAll(participants);
        for (Participant participant : participantsList) {
            if (participant.getPhoneNumber() != null) {
                TargetNumberRequest buildTargetNumber = TargetNumberRequest.builder()
                        .number(List.of(participant.getPhoneNumber()))
                        .build();
                restTemplateService.sendMessageRegisterWhatsapp(buildTargetNumber);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<Participant> getAll(SearchUserRequest request) {
        Specification<Participant> specification = getParticipantSpecification(request);
        if (request.getPage() <= 0) request.setPage(1);
        if (request.getSize() <= 0) request.setSize(10);
        Pageable pageRequest = PageRequest.of(request.getPage()-1, request.getSize());
        Page<Participant> findAll = participantRepository.findAll(specification,pageRequest);
        return findAll;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse getById(String request) {
        Optional<Participant> optionalParticipant = participantRepository.findById(request);
        if (!optionalParticipant.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found");

        return UserResponse.builder()
                .id(optionalParticipant.get().getId())
                .name(optionalParticipant.get().getName())
                .gender(optionalParticipant.get().getGender())
                .address(optionalParticipant.get().getAddress())
                .email(optionalParticipant.get().getUserCredential().getEmail())
                .phoneNumber(optionalParticipant.get().getPhoneNumber())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(ProfileUpdateRequest request) {
        UserResponse findId = getById(request.getId());
        UserCredential userCredential =(UserCredential) userService.loadUserByUsername(findId.getEmail());

        Participant buildParticipant = Participant.builder()
                .id(findId.getId())
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(userCredential)
                .build();

        Participant participant = participantRepository.saveAndFlush(buildParticipant);

        return UserResponse.builder()
                .id(participant.getId())
                .name(participant.getName())
                .gender(participant.getGender())
                .address(participant.getAddress())
                .email(participant.getUserCredential().getEmail())
                .phoneNumber(participant.getPhoneNumber())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updatePassword(UpdatePasswordRequest request) {
        UserResponse participan = getById(request.getId());

        String updateUsercredential = userService.update(
                UserUpdateRequest.builder()
                        .email(participan.getEmail())
                        .password(request.getPassword())
                        .new_password(request.getNew_password())
                        .build()
        );
        return updateUsercredential;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(String id) {
        UserResponse participant = getById(id);
        UserCredential userCredential =(UserCredential) userService.loadUserByUsername(participant.getEmail());
        participantRepository.deleteById(participant.getId());
        String delete = userService.delete(userCredential);
        return delete;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Participant getByParticipantId(String id) {
        return participantRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Participant> getAllParticipant() {
       return participantRepository.findAll();
    }

    private static Specification<Participant> getParticipantSpecification(SearchUserRequest request) {
        Specification<Participant> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null){
                Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" +request.getName() + "%");
                predicates.add(namePredicate);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        return specification;
    }

    @Override
    public Participant getByUserCredential(UserCredential userCredential) {
        return participantRepository.findByUserCredential(userCredential).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"participant not found"));
    }

    @Override
    public String getClasses(Participant participant) {
        DetailClassParticipant classesName = classDetailService.getByParticipantId(participant);
        if (classesName == null) return "no class";
        return classesName.getClasses().getName();
    }
}
