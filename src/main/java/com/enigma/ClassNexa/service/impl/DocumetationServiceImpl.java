package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Documentation;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.model.DocumentationClaim;
import com.enigma.ClassNexa.model.request.SearchDocumentationRequest;
import com.enigma.ClassNexa.repository.DocumetationRepository;
import com.enigma.ClassNexa.service.DocumetationService;
import com.enigma.ClassNexa.service.ScheduleService;
import com.enigma.ClassNexa.service.TrainerService;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumetationServiceImpl implements DocumetationService {
    private final TrainerService trainerService;
    private final ScheduleService scheduleService;
    private final DocumetationRepository documetationRepository;


//    private final String FILE_PATH = "classpath:static"+"\n"+"mage";
    private final String FILE_PATH2 = "/src/main/resources/image/";
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Documentation create(MultipartFile multipartFile, SearchDocumentationRequest request) throws IOException {
        Trainer byId = trainerService.getByTrainerId(request.getTrainer());
        Schedule byIdSchedule = scheduleService.getByIdSchedule(request.getSchedule());

        String projectname = "ClassNexa";
        Path projectDirectory = Paths.get(System.getProperty("user.dir"), projectname);

        String filePathString = projectDirectory.toAbsolutePath()+FILE_PATH2+multipartFile.getOriginalFilename();

        Documentation documentation = Documentation.builder()
                .fileName(multipartFile.getOriginalFilename())
                .trainer(byId)
                .schedule(byIdSchedule)
                .build();
        Documentation save = documetationRepository.save(documentation);
        multipartFile.transferTo(new File(filePathString));
        return save;
    }

    @Override
    public byte[] download(String filename) throws IOException {
        Documentation byName = documetationRepository.findByFileName(filename);
        String projectname = "ClassNexa";
        Path projectDirectory = Paths.get(System.getProperty("user.dir"), projectname);
        String filepath = projectDirectory.toAbsolutePath()+FILE_PATH2+byName.getFileName();
        DocumentationClaim documentationClaim = DocumentationClaim.builder()
                .id(byName.getId())
                .fileName(byName.getFileName())
                .trainer(byName.getTrainer().getId())
                .schedule(byName.getSchedule().getId())
                .path(filepath)
                .build();

        return Files.readAllBytes(new File(filepath).toPath());
    }

    @Override
    public List<Documentation> getAll() {
        List<Documentation> all = documetationRepository.findAll();
        return all;
    }

    @Override
    public Documentation getById(String id) {
        Optional<Documentation> byId = documetationRepository.findById(id);
        Documentation documentation = Documentation.builder()
                .id(id)
                .fileName(byId.get().getFileName())
                .trainer(byId.get().getTrainer())
                .schedule(byId.get().getSchedule())
                .build();
        return documentation;
    }

    @Override
    public Documentation update(MultipartFile multipartFile, SearchDocumentationRequest documentation) throws IOException {
        Trainer byId = trainerService.getByTrainerId(documentation.getTrainer());
        Schedule byIdSchedule = scheduleService.getByIdSchedule(documentation.getSchedule());

        String projectname = "ClassNexa";
        Path projectDirectory = Paths.get(System.getProperty("user.dir"), projectname);

        String filePathString = projectDirectory.toAbsolutePath()+FILE_PATH2+multipartFile.getOriginalFilename();

        Documentation documentation1 = Documentation.builder()
                .id(documentation.getId())
                .fileName(multipartFile.getOriginalFilename())
                .trainer(byId)
                .schedule(byIdSchedule)
                .build();
        Documentation save = documetationRepository.save(documentation1);
        multipartFile.transferTo(new File(filePathString));
        return save;
    }

    @Override
    public String delete(String id) {
        documetationRepository.deleteById(id);
        return "ok";
    }
}
