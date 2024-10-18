package com.skillspace.career.dto;

import com.skillspace.career.Model.Career;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CareerProgramRequest {

    private Career career;
    private Long companyId;
}
