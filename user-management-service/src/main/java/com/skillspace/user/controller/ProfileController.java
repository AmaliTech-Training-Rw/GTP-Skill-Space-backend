package com.skillspace.user.controller;


import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.dto.UpdateCompany;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.service.CompanyService;
import com.skillspace.user.service.PersonalDetailsService;
import com.skillspace.user.util.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users/profiles")
public class ProfileController {

    private final PersonalDetailsService service;

    private final CompanyService companyService;

    @Autowired
    public ProfileController(PersonalDetailsService service, CompanyService companyService) {
        this.service = service;
        this.companyService = companyService;
    }


    @PatchMapping(value = "/talent-details/{talentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<PersonalDetails>>
    updateTalentDetails(@PathVariable Long talentId,
                        @RequestPart("personalDetailsDto") @Valid PersonalDetailsDto personalDetails,
                        @RequestPart(value = "profilePic", required = false) MultipartFile profilePic,
                        @RequestPart(value = "cv", required = false) MultipartFile cv
    ) {

        return new ResponseEntity<>(new CustomResponse<>("Profile updated successfully",
                HttpStatus.OK.value(), service.updatePersonalDetails(talentId, personalDetails, profilePic, cv)), HttpStatus.OK);

    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<PersonalDetails>> findById(@PathVariable UUID id) {
        Optional<PersonalDetails> telentDetails = service.findById(id);

        if (telentDetails.isPresent()) {
            return new ResponseEntity<>(new CustomResponse<>("Talent details retrieved successfully",
                    HttpStatus.OK.value(), telentDetails.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CustomResponse<>("Talent not found", HttpStatus.NOT_FOUND.value(),
                    null), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{talentId}")
    public ResponseEntity<CustomResponse<PersonalDetails>> findByTalentId(@PathVariable Long talentId) {
        Optional<PersonalDetails> telentDetails = service.findTalentProfile(talentId);

        return telentDetails.map(personalDetails -> new ResponseEntity<>(new CustomResponse<>(
                        "Talent details retrieved successfully", HttpStatus.OK.value(), personalDetails), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new CustomResponse<>("Talent not found",
                        HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND));
    }


    @PatchMapping(value = "company/{companyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<Company>>
    updateCompany(@PathVariable Long companyId,
                  @RequestPart(value = "companyDto", required = false) @Valid UpdateCompany company,
                  @RequestPart(value = "certificate", required = false) MultipartFile certificate,
                  @RequestPart(value = "logo", required = false) MultipartFile logo) {

        CustomResponse<Company> response = companyService.updateCompany(companyId, company, certificate, logo);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

    }
}
