package com.skillspace.user.controller;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.entity.Education;
import com.skillspace.user.service.EducationService;
import com.skillspace.user.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/talent-educations")
public class EducationController {

    private final EducationService educationService;

    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PatchMapping("/{talentId}")
    public ResponseEntity<CustomResponse<Education>> createEducation(
            @PathVariable Long talentId,
            @RequestPart(value = "nameOfInstitution", required = false) String nameOfInstitution,
            @RequestPart(value = "addressOfInstitution", required = false) String addressOfInstitution,
            @RequestPart(value = "country", required = false) String country,
            @RequestPart(value = "nameOfProgram", required = false) String nameOfProgram,
            @RequestPart(value = "programStatus", required = false) String programStatus,
            @RequestParam(value = "dateCommencement", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCommencement,
            @RequestParam(value = "dateCompleted", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCompleted,
            @RequestPart(value = "transcripts", required = false) MultipartFile transcripts) {

        EducationDto educationDto = new EducationDto();
        educationDto.setTalentId(talentId);
        educationDto.setNameOfInstitution(nameOfInstitution);
        educationDto.setAddressOfInstitution(addressOfInstitution);
        educationDto.setCountry(country);
        educationDto.setNameOfProgram(nameOfProgram);
        educationDto.setProgramStatus(programStatus);
        educationDto.setDateCommencement(dateCommencement);
        educationDto.setDateCompleted(dateCompleted);
        educationDto.setTranscripts(transcripts);

        Education updatedEducation = educationService.updateEducationDetails(educationDto);
        return new ResponseEntity<>( new CustomResponse<>( "Talent Education details updated successfully", HttpStatus.OK.value(), updatedEducation), HttpStatus.OK );
    }
}
