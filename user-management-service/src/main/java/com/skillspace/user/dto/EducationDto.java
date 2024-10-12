package com.skillspace.user.dto;

import com.skillspace.user.entity.ProgramStatus;
import com.skillspace.user.util.customValidation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EducationDto {

    private Long talentId;

    @NotBlank(message = "Name of the institution cannot be blank")
    private String nameOfInstitution;

    @NotBlank(message = "Address of the institution cannot be blank")
    private String addressOfInstitution;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotBlank(message = "Name of the program cannot be blank")
    private String nameOfProgram;

    @NotNull(message = "Program status cannot be null")
    @ValueOfEnum(enumClass = ProgramStatus.class)
    private ProgramStatus programStatus;

    @NotNull(message = "Commencement date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCommencement;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCompleted;
}
