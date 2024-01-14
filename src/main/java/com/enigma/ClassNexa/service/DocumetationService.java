package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Documentation;
import com.enigma.ClassNexa.model.request.SearchDocumentationRequest;
import com.enigma.ClassNexa.model.response.DocumentationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumetationService {
    DocumentationResponse create(MultipartFile multipartFile, SearchDocumentationRequest request) throws IOException;
    byte[] download(String filename) throws IOException;
    List<Documentation> getAll();
    DocumentationResponse getById(String id);
    DocumentationResponse update(MultipartFile multipartFile, SearchDocumentationRequest documentation) throws IOException;
    String delete(String id);
}
