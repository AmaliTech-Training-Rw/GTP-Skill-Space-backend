package com.skillspace.user.controller;

import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.dto.UpdateCompany;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.NotificationPreference;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.service.CompanyService;
import com.skillspace.user.service.PersonalDetailsService;
import com.skillspace.user.util.CustomResponse;
import com.skillspace.user.util.customValidation.ValidContact;
import com.skillspace.user.util.helper.HelperMethods;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
                        @RequestPart(value = "firstName", required = false) String firstName,
                        @RequestPart(value = "lastName", required = false) String lastName,
                        @RequestPart(value = "contact", required = false) @ValidContact String contact,
                        @RequestPart(value = "location", required = false) String location,
                        @RequestParam(value = "available", required = false) boolean available,
                        @RequestParam(value = "badges", required = false) String badges,
                        @RequestPart(value = "notificationPreference", required = false) String notificationPreference,
                        @RequestPart(value = "portfolio", required = false) @URL(message = "Invalid URL format for portfolio") String portfolio,
                        @RequestParam(value = "dob", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
                        @RequestPart(value = "bio", required = false) String bio,
                        @RequestPart(value = "profilePic", required = false) MultipartFile profilePic,
                        @RequestPart(value = "socialMedia", required = false) Map<String, String> socialMedia,
                        @RequestPart(value = "cv", required = false) MultipartFile cv
    ) {

        HelperMethods helperMethods = new HelperMethods();
        PersonalDetailsDto personalDetails = helperMethods.buildPersonalDetailsDto(
                talentId, firstName, lastName, contact, location, available, badges, notificationPreference,
                portfolio, dob, bio, profilePic, socialMedia, cv);

        return new ResponseEntity<>(new CustomResponse<>("Profile updated successfully",
                HttpStatus.OK.value(), service.updatePersonalDetails(talentId, personalDetails)), HttpStatus.OK);
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
                  @RequestPart(value = "name", required = false) @Size(max = 50, message = "Company name cannot exceed 100 characters") String name,
                  @RequestPart(value = "contact", required = false) @ValidContact String contact,
                  @RequestPart(value = "certificate", required = false) MultipartFile certificate,
                  @RequestPart(value = "logo", required = false) MultipartFile logo,
                  @RequestPart(value = "website", required = false) @URL(message = "Invalid URL format for website") String website) {

        UpdateCompany company = new UpdateCompany();
        company.setName(name);
        company.setContact(contact);
        company.setCertificate(certificate);
        company.setWebsite(website);
        company.setLogo(logo);

        CustomResponse<Company> response = companyService.updateCompany(companyId, company);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
