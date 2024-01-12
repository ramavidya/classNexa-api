package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.repository.DetailClassParticipantRepository;
import com.enigma.ClassNexa.service.ClassDetailService;
import com.enigma.ClassNexa.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassDetailServiceImpl implements ClassDetailService {

    private final DetailClassParticipantRepository detailClassParticipantRepository;
    private final ValidationUtils validationUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DetailClassParticipant createOrUpdate(DetailClassParticipant detailClassParticipant) {
        validationUtils.validate(detailClassParticipant);
        return detailClassParticipantRepository.save(detailClassParticipant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String detailClassParticipant) {
        validationUtils.validate(detailClassParticipant);
        detailClassParticipantRepository.deleteById(detailClassParticipant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DetailClassParticipant> getByClassId(String id) {
        validationUtils.validate(id);
        return detailClassParticipantRepository.findByClassesId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DetailClassParticipant> deleteByClassId(String id) {
        validationUtils.validate(id);
        return detailClassParticipantRepository.deleteByClassesId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DetailClassParticipant getByParticipantId(Participant participant) {
        validationUtils.validate(participant);
        return detailClassParticipantRepository.findByParticipant(participant).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DetailClassParticipant> getAllDetail() {
        return detailClassParticipantRepository.findAll();
    }


}
