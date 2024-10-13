package com.skillspace.user.controller;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.dto.UpdateEducationDto;
import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.ProgramStatus;
import com.skillspace.user.service.EducationService;
import com.skillspace.user.util.CustomResponse;
import com.skillspace.user.util.helper.HelperMethods;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/users/talent/educations")
public class EducationController {

    private final EducationService educationService;

    private HelperMethods helperMethods;


    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<Education>> createEducation(@RequestPart(value = "educationDto") @Valid EducationDto educationDto,
                                                                     @RequestPart(value = "transcripts") MultipartFile transcripts) {

        CustomResponse<Education> response = educationService.createEducationDetails(educationDto, transcripts);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<Education>> updateEducation(
            @PathVariable UUID id,
            @RequestPart(value = "educationDto", required = false) @Valid UpdateEducationDto educationDto,
            @RequestPart(value = "transcripts", required = false) MultipartFile transcripts) {

        CustomResponse<Education> response = educationService.updateEducationDetails(id, educationDto, transcripts);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

    }
}
