package com.skillspace.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class EducationDto {

    private String talentId;
    private MultipartFile transcripts;
    private String nameOfInstitution;
    private String addressOfInstitution;
    private String country;
    private String nameOfProgram;
    private String programStatus;
    private LocalDate dateCommencement;
    private LocalDate dateCompleted;
}
