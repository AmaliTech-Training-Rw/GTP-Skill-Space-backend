package com.skillspace.user.dto;

import com.skillspace.user.entity.ProgramStatus;
import com.skillspace.user.util.customValidation.ValueOfEnum;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UpdateEducationDto {

    @Size(max = 50, message = "Name of the institution cannot exceed 50 characters")
    private String nameOfInstitution;

    @Size(max = 50, message = "Address of the institution cannot exceed 50 characters")
    private String addressOfInstitution;

    @Size(max = 50, message = "Country cannot be blank")
    private String country;

    @Size(max = 50, message = "Name of the program cannot exceed 50 characters")
    private String nameOfProgram;

    @ValueOfEnum(enumClass = ProgramStatus.class)
    private ProgramStatus programStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCommencement;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCompleted;
}
