package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Documentation;
import com.enigma.ClassNexa.model.request.SearchDocumentationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumetationService {
    Documentation create(MultipartFile multipartFile, SearchDocumentationRequest request) throws IOException;
    byte[] download(String filename) throws IOException;
    List<Documentation> getAll();
    Documentation getById(String id);
    Documentation update(MultipartFile multipartFile, SearchDocumentationRequest documentation) throws IOException;
    String delete(String id);
}
