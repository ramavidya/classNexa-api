package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Documentation;
import com.enigma.ClassNexa.model.request.SearchDocumentationRequest;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.DocumetationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DocumetationController {
    private final DocumetationService documetationService;

    @PostMapping(path = "/api/documentation/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> storeFilesIntoDB(@RequestPart(name = "multipartFile") MultipartFile multipartFile,
                                                   @RequestParam String trainer,
                                                   @RequestParam String schedule) throws IOException {
        SearchDocumentationRequest request = SearchDocumentationRequest.builder()
                .trainer(trainer)
                .schedule(schedule)
                .build();
        Documentation response = documetationService.create(multipartFile, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/documentation/download/{fileName}")
    public ResponseEntity<byte[]> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = documetationService.download(fileName);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
    }

    @GetMapping(path = "/api/documentation")
    public ResponseEntity<?> getAll(){
        List<Documentation> all = documetationService.getAll();
        WebResponse<List<Documentation>> response = WebResponse.<List<Documentation>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("All data schedule")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/api/documentation/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Documentation all = documetationService.getById(id);
        WebResponse<Documentation> response = WebResponse.<Documentation>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Get data schedule by id")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/api/documentation/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestPart(name = "multipartFile") MultipartFile multipartFile,
                                              @RequestParam String id,
                                              @RequestParam String trainer,
                                              @RequestParam String schedule) throws IOException {
        SearchDocumentationRequest request = SearchDocumentationRequest.builder()
                .id(id)
                .trainer(trainer)
                .schedule(schedule)
                .build();
        Documentation response = documetationService.update(multipartFile, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(path = "/api/documentation/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String all = documetationService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Data deleted")
                .data("ok")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
