package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.entity.Participant;

import java.util.List;

public interface ClassDetailService {

    DetailClassParticipant createOrUpdate(DetailClassParticipant detailClassParticipant);
    List<DetailClassParticipant> getByClassId(String id);
    List<DetailClassParticipant> deleteByClassId(String id);
    DetailClassParticipant getByParticipantId (Participant participant);
    List<DetailClassParticipant> getAllDetail();
    void deleteById(String detailClassParticipant);


}
