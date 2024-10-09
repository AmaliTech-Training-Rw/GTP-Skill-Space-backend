package com.skillspace.user.controller;

import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.dto.UpdateCompany;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.service.CompanyService;
import com.skillspace.user.service.PersonalDetailsService;
import com.skillspace.user.util.CustomResponse;
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
@RequestMapping("/api/user-profiles")
public class ProfileController {

    private final PersonalDetailsService service;

    private final CompanyService companyService;

    @Autowired
    public ProfileController(PersonalDetailsService service, CompanyService companyService) {
        this.service = service;
        this.companyService = companyService;
    }

    @PatchMapping(value = "/talent-details/{talentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<PersonalDetails>> create(@PathVariable Long talentId,
                                                                  @RequestParam(value = "firstName", required = false) @Size(max = 50, message = "First name cannot exceed 50 characters") String firstName,
                                                                  @RequestParam(value = "lastName", required = false) @Size(max = 50, message = "First name cannot exceed 50 characters") String lastName,
                                                                  @RequestParam(value = "contact", required = false) @Pattern(regexp = "^\\+?[0-9. ()-]{7,}$", message = "Invalid contact number") String contact,
                                                                  @RequestParam(value = "location", required = false) String location,
                                                                  @RequestParam(value = "available", required = false) boolean available,
                                                                  @RequestParam(value = "badges", required = false) String badges,
                                                                  @RequestParam(value = "notificationPreference", required = false) String notificationPreference,
                                                                  @RequestParam(value = "portfolio", required = false) @URL(message = "Invalid URL format for portfolio") String portfolio,
                                                                  @RequestParam(value = "dob", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
                                                                  @RequestParam(value = "bio", required = false) String bio,
                                                                  @RequestPart(value = "profilePic", required = false) MultipartFile profilePic,
                                                                  @RequestPart(value = "socialMedia", required = false) Map<String, String> socialMedia,
                                                                  @RequestPart(value = "cv", required = false) MultipartFile cv
                                                  ) {

        PersonalDetailsDto personalDetails = new PersonalDetailsDto();
        personalDetails.setTalentId(talentId);
        personalDetails.setFirstName(firstName);
        personalDetails.setLastName(lastName);
        personalDetails.setContact(contact);
        personalDetails.setLocation(location);
        personalDetails.setAvailable(available);
        personalDetails.setBadges(badges);
        personalDetails.setNotificationPreference(notificationPreference);
        personalDetails.setPortfolio(portfolio);
        personalDetails.setCv(cv);
        personalDetails.setProfilePic(profilePic);
        personalDetails.setSocialMedia(socialMedia);
        personalDetails.setBio(bio);
        personalDetails.setDob(dob);
        personalDetails.setNotificationPreference(notificationPreference);

        return new ResponseEntity<>(new CustomResponse<>("Profile updated successfully", HttpStatus.OK.value(), service.updatePersonalDetails(personalDetails)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<PersonalDetails>> findById(@PathVariable UUID id) {
        Optional<PersonalDetails> telentDetails = service.findById(id);

        if (telentDetails.isPresent()) {
            return new ResponseEntity<>(new CustomResponse<>("Talent details retrieved successfully", HttpStatus.OK.value(), telentDetails.get()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new CustomResponse<>("Talent not found", HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{talentId}")
    public ResponseEntity<CustomResponse<PersonalDetails>> findByTalentId(@PathVariable Long talentId) {
        Optional<PersonalDetails> telentDetails = service.findTalentProfile(talentId);

        return telentDetails.map(personalDetails -> new ResponseEntity<>(new CustomResponse<>("Talent details retrieved successfully", HttpStatus.OK.value(), personalDetails), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new CustomResponse<>("Talent not found", HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND));
    }

    @PutMapping("company/{companyId}")
    public ResponseEntity<CustomResponse<Company>> updateCompany(@PathVariable Long companyId,
                                                 @RequestParam(value = "name", required = false) @NotBlank(message = "Company name is required") @Size(max = 100, message = "Company name cannot exceed 100 characters")String name,
                                                 @RequestParam(value = "contact", required = false) @NotBlank(message = "Contact is required") String contact,
                                                 @RequestParam(value = "certificate", required = false) MultipartFile certificate,
                                                 @RequestParam("logo") MultipartFile logo,
                                                 @RequestParam(value = "website", required = false) @URL(message = "Invalid URL format for website") String website) {

        UpdateCompany company = new UpdateCompany();
        company.setName(name);
        company.setContact(contact);
        company.setCertificate(certificate);
        company.setWebsite(website);
        company.setLogo(logo);

        Company updatedCompany = companyService.updateCompany(companyId, company);
        return new ResponseEntity<>(new CustomResponse<>("Company details updated successfully", HttpStatus.OK.value(), updatedCompany), HttpStatus.OK);
    }
}
