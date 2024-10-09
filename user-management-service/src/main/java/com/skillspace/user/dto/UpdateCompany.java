package com.skillspace.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateCompany {
    private String contact;
    private String name;
    private MultipartFile certificate;
    private MultipartFile logo;
    private String website;
}
