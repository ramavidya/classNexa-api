package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.request.SearchUserRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.response.PagingResponse;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
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
        List<UserResponse> userGetResponses = new ArrayList<>();
        for (Participant participant : pageParticipant.getContent()) {
            UserResponse buildResponse = UserResponse.builder()
                    .id(participant.getId())
                    .name(participant.getName())
                    .gender(participant.getGender())
                    .address(participant.getAddress())
                    .email(participant.getUserCredential().getEmail())
                    .phoneNumber(participant.getPhoneNumber()).build();
            userGetResponses.add(buildResponse);
        }
        WebResponse<List<UserResponse>> response = WebResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get participant")
                .paging(new PagingResponse(pageParticipant.getNumber()+1,
                        pageParticipant.getSize(),
                        pageParticipant.getTotalPages(),
                        pageParticipant.getTotalElements()))
                .data(userGetResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        UserResponse getById = participantService.getById(id);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get participant")
                .data(getById)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ProfileUpdateRequest request){
        UserResponse updateResponse = participantService.update(request);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
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

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update password")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String delete = participantService.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly delete participant")
                .data(delete)
                .build();

        return ResponseEntity.ok(response);
    }

}
