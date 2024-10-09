package com.skillspace.user.controller;

import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.service.PersonalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal-details")
public class PersonalDetailsController {

    private final PersonalDetailsService service;

    @Autowired
    public PersonalDetailsController(PersonalDetailsService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PersonalDetails> create(@RequestParam(value = "talentId", required = false) String talentId,
                                                  @RequestParam(value = "location", required = false) String location,
                                                  @RequestParam(value = "available", required = false) boolean available,
                                                  @RequestParam(value = "badges", required = false) String badges,
                                                  @RequestParam(value = "notificationPreference", required = false) String notificationPreference,
                                                  @RequestParam(value = "portfolio", required = false) String portfolio,
                                                  @RequestParam(value = "dob", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
                                                  @RequestParam(value = "bio", required = false) String bio,
                                                  @RequestPart(value = "profilePic", required = false) MultipartFile profilePic,
                                                  @RequestPart(value = "socialMedia", required = false) Map<String, String> socialMedia,
                                                  @RequestPart(value = "cv", required = false) MultipartFile cv
                                                  ) {

        PersonalDetailsDto personalDetails = new PersonalDetailsDto();
        personalDetails.setTalentId(talentId);
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

        return ResponseEntity.ok(service.create(personalDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDetails> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping
//    public ResponseEntity<List<PersonalDetails>> findAll() {
//        return ResponseEntity.ok(service.findAll());
//    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalDetails> update(@PathVariable UUID id, @RequestBody PersonalDetails personalDetails) {

        return ResponseEntity.ok(service.update(personalDetails));
    }
}
