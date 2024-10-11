package com.skillspace.user.controller;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.ProgramStatus;
import com.skillspace.user.service.EducationService;
import com.skillspace.user.util.CustomResponse;
import com.skillspace.user.util.helper.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/talent-educations")
public class EducationController {

    private final EducationService educationService;

    private HelperMethods helperMethods;


    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Education>> createEducation(
            @RequestParam Long talentId,
            @RequestPart(value = "nameOfInstitution") String nameOfInstitution,
            @RequestPart(value = "addressOfInstitution") String addressOfInstitution,
            @RequestPart(value = "country") String country,
            @RequestPart(value = "nameOfProgram") String nameOfProgram,
            @RequestPart(value = "programStatus") String programStatus,
            @RequestParam(value = "dateCommencement") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCommencement,
            @RequestParam(value = "dateCompleted") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCompleted,
            @RequestPart(value = "transcripts") MultipartFile transcripts) {

        helperMethods = new HelperMethods();
        EducationDto educationDto = helperMethods.buildEducationDto(talentId, nameOfInstitution, addressOfInstitution, country, nameOfProgram, programStatus, dateCommencement, dateCompleted, transcripts);

        CustomResponse<Education> response = educationService.createEducationDetails(educationDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse<Education>> updateEducation(
            @PathVariable UUID id,
            @RequestPart(value = "nameOfInstitution", required = false) String nameOfInstitution,
            @RequestPart(value = "addressOfInstitution", required = false) String addressOfInstitution,
            @RequestPart(value = "country", required = false) String country,
            @RequestPart(value = "nameOfProgram", required = false) String nameOfProgram,
            @RequestPart(value = "programStatus", required = false) String programStatus,
            @RequestParam(value = "dateCommencement", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCommencement,
            @RequestParam(value = "dateCompleted", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCompleted,
            @RequestPart(value = "transcripts", required = false) MultipartFile transcripts) {

        helperMethods = new HelperMethods();
        EducationDto educationDto = helperMethods.buildEducationDto( null, nameOfInstitution, addressOfInstitution, country, nameOfProgram, programStatus, dateCommencement, dateCompleted, transcripts);

        CustomResponse<Education> response = educationService.updateEducationDetails(id, educationDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
