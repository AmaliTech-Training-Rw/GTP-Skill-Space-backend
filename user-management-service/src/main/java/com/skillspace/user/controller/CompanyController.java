package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.entity.Company;
import com.skillspace.user.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/auth/register")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(value ="/company", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerCompany(@Valid @RequestPart CompanyRegistrationRequest request, @RequestPart(value = "certificate", required = false) MultipartFile certificate,
                                             @RequestPart(value = "logo", required = false) MultipartFile logo) {
        Company registeredCompany = companyService.registerCompany(request, certificate, logo);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompany);
    }

    @GetMapping("/all")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{name}")
    public Company getCompanyByName(@PathVariable String name) {
        return companyService.findByName(name);
    }
}


