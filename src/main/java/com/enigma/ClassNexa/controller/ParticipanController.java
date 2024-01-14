package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.request.SearchUserRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.response.*;
import com.enigma.ClassNexa.service.ClassDetailService;
import com.enigma.ClassNexa.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/participant")
public class ParticipanController {

    private final ParticipantService participantService;

    @PreAuthorize("hasRole('PARTICIPANT')")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size){
        SearchUserRequest buildSearch = SearchUserRequest.builder().name(name).page(page).size(size).build();
        Page<Participant> pageParticipant = participantService.getAll(buildSearch);

        List<ParticipantGetResponse> userGetResponses = new ArrayList<>();
        for (Participant participant : pageParticipant.getContent()) {
            String classes = participantService.getClasses(participant);
            ParticipantGetResponse buildResponse = ParticipantGetResponse.builder()
                    .id(participant.getId())
                    .name(participant.getName())
                    .gender(participant.getGender())
                    .address(participant.getAddress())
                    .email(participant.getUserCredential().getEmail())
                    .phoneNumber(participant.getPhoneNumber())
                    .classes(classes)
                    .build();
            userGetResponses.add(buildResponse);
        }
        WebResponse<List<ParticipantGetResponse>> response = WebResponse.<List<ParticipantGetResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get participant")
                .pagingResponse(new PagingResponse(pageParticipant.getTotalElements(),
                        pageParticipant.getTotalPages(),
                        pageParticipant.getNumber()+1,
                        pageParticipant.getSize()))
                .data(userGetResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Participant participant = participantService.getByParticipantId(id);
        String classes = participantService.getClasses(participant);
        ParticipantGetResponse buildResponse = ParticipantGetResponse.builder()
                .id(participant.getId())
                .name(participant.getName())
                .gender(participant.getGender())
                .address(participant.getAddress())
                .email(participant.getUserCredential().getEmail())
                .phoneNumber(participant.getPhoneNumber())
                .classes(classes)
                .build();

        CommonResponse<ParticipantGetResponse> response = CommonResponse.<ParticipantGetResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get participant")
                .data(buildResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ProfileUpdateRequest request){
        UserResponse updateResponse = participantService.update(request);

        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update participant")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @PutMapping(path = "/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request){
        String updateResponse = participantService.updatePassword(request);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update password")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String delete = participantService.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly delete participant")
                .data(delete)
                .build();

        return ResponseEntity.ok(response);
    }

}
