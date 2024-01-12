package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.*;
import com.enigma.ClassNexa.model.request.ScheduleRequest;
import com.enigma.ClassNexa.model.response.ScheduleResponse;
import com.enigma.ClassNexa.repository.DetailClassParticipantRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClassesService classCNService;
    private final TrainerService trainerService;
    private final JavaMailSender javaMailSender;
    private final ClassDetailService classDetailService;
    private final ParticipantService participantService;
    private final UserService userService;
    private final DetailClassParticipantRepository detailClassParticipantRepository;

    private ScheduleResponse toScheduleResponse(Schedule schedule){
        Classes byIdClass = classCNService.getId(schedule.getClasses_id().getId());
        Trainer byIdTrainer = trainerService.getByTrainerId(byIdClass.getTrainer().getId());
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .meeting_link(schedule.getMeeting_link())
                .start_class(schedule.getStart_class())
                .end_class(schedule.getEnd_class())
                .classes_id(schedule.getClasses_id().getId())
                .classes_name(byIdClass.getName())
                .trainer(byIdTrainer.getName())
                .build();
    }

    public void sendEmail(String to, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("priyqmbido@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Selamat Datang di ClassNexa");
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleResponse create(ScheduleRequest request) {
        Classes byIdClass = classCNService.getId(request.getClasses_id());
        Schedule schedule = Schedule.builder()
                .meeting_link(request.getMeeting_link())
                .start_class(request.getStart_class())
                .end_class(request.getEnd_class())
                .classes_id(byIdClass)
                .build();
        Schedule save = scheduleRepository.save(schedule);

        log.info(save.getClasses_id().getId());
        List<DetailClassParticipant> byClassId = detailClassParticipantRepository.findByClassesId(save.getClasses_id().getId());

        for (int i=0;i<byClassId.size();i++){
            Participant byParticipantId = participantService.getByParticipantId(byClassId.get(i).getParticipant().getId());
            UserCredential userCredential = userService.loadUserById(byParticipantId.getUserCredential().getId());

            String message = "Kami sangat senang bisa menyambut Anda sebagai bagian dari Bootcamp"+byParticipantId.getName()+
                    "! Dengan antusiasme, kami ingin mengucapkan selamat datang kepada Anda dan berharap bahwa pengalaman yang Anda dapatkan di sini akan menjadi perjalanan yang luar biasa.\n" +
                    "\n" +
                    "Di bootcamp ini, Anda akan memiliki kesempatan untuk belajar, berkolaborasi, dan berkembang bersama para profesional terbaik dalam industri ini. Kami percaya bahwa Anda memiliki potensi besar dan kami sangat bersemangat untuk melihat kontribusi luar biasa yang akan Anda berikan.\n" +
                    "\n" +
                    "Jangan ragu untuk bertanya kepada instruktur atau sesama peserta jika Anda membutuhkan bantuan dalam menjalani program ini. Jadilah terbuka untuk belajar hal-hal baru dan manfaatkan setiap kesempatan yang ada.\n" +
                    "\n" +
                    "Tetap semangat, jadilah diri Anda sendiri, dan nikmati perjalanan ini. Kami yakin Anda akan menghasilkan karya-karya luar biasa.\n" +
                    "\n" +
                    "Salam hangat, Tim ClassNexa";
            sendEmail(userCredential.getEmail(), message);
        }
        return toScheduleResponse(save);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ScheduleResponse> getAll() {
        List<Schedule> all = scheduleRepository.findAll();
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        for (int i = 0; i<all.size();i++){
            Classes byIdClass = classCNService.getId(all.get(i).getClasses_id().getId());
            Trainer byIdTrainer = trainerService.getByTrainerId(byIdClass.getTrainer().getId());
            ScheduleResponse scheduleResponse =ScheduleResponse.builder()
                    .id(all.get(i).getId())
                    .meeting_link(all.get(i).getMeeting_link())
                    .start_class(all.get(i).getStart_class())
                    .end_class(all.get(i).getEnd_class())
                    .classes_id(all.get(i).getClasses_id().getId())
                    .classes_name(byIdClass.getName())
                    .trainer(byIdTrainer.getName())
                    .build();
            scheduleResponses.add(scheduleResponse);

        }
        return scheduleResponses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(String id) {
        scheduleRepository.deleteById(id);
        return "ok";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleResponse update(ScheduleRequest request) {

        Optional<Schedule> byIdSchedule = scheduleRepository.findById(request.getId());
        Classes byIdClass = classCNService.getId(request.getClasses_id());
        Schedule schedule = Schedule.builder()
                .id(byIdSchedule.get().getId())
                .meeting_link(request.getMeeting_link())
                .start_class(request.getStart_class())
                .end_class(request.getEnd_class())
                .classes_id(byIdClass)
                .build();

        Schedule save = scheduleRepository.save(schedule);

        return toScheduleResponse(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleResponse getById(String id) {
        if (id.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID schedule not found");

        Optional<Schedule> byId = scheduleRepository.findById(id);
        Classes byIdClass = classCNService.getId(byId.get().getClasses_id().getId());
        Trainer byIdTrainer = trainerService.getByTrainerId(byIdClass.getTrainer().getId());
        ScheduleResponse scheduleResponse =ScheduleResponse.builder()
                .id(byId.get().getId())
                .meeting_link(byId.get().getMeeting_link())
                .start_class(byId.get().getStart_class())
                .end_class(byId.get().getEnd_class())
                .classes_id(byId.get().getClasses_id().getId())
                .classes_name(byIdClass.getName())
                .trainer(byIdTrainer.getName())
                .build();
        return scheduleResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Schedule getByIdSchedule(String id) {
        Optional<Schedule> byId = scheduleRepository.findById(id);
        Schedule schedule = Schedule.builder()
                .id(byId.get().getId())
                .meeting_link(byId.get().getMeeting_link())
                .start_class(byId.get().getStart_class())
                .end_class(byId.get().getEnd_class())
                .classes_id(byId.get().getClasses_id())
                .build();
        return schedule;
    }

}
