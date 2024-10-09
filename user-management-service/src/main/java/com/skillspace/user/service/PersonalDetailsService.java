package com.skillspace.user.service;

import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.repository.PersonalDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonalDetailsService {

    private final PersonalDetailsRepository repository;

    private final FileUploadService fileUploadService;

    @Autowired
    public PersonalDetailsService(PersonalDetailsRepository repository, FileUploadService fileUploadService) {
        this.repository = repository;
        this.fileUploadService = fileUploadService;
    }

    public PersonalDetails create(PersonalDetailsDto personalDetails) {
        PersonalDetails newPersonalDetails = new PersonalDetails();
        newPersonalDetails.setId(UUID.randomUUID());
        newPersonalDetails.setBio(personalDetails.getBio());
        newPersonalDetails.setDob(personalDetails.getDob());
        newPersonalDetails.setBadges(Arrays.asList(personalDetails.getBadges().split(",")));
        newPersonalDetails.setAvailable(personalDetails.isAvailable());
        newPersonalDetails.setLocation(personalDetails.getLocation());
        newPersonalDetails.setNotificationPreference(personalDetails.getNotificationPreference());
        newPersonalDetails.setPortfolio(personalDetails.getPortfolio());
        newPersonalDetails.setTalentId(personalDetails.getTalentId());
        newPersonalDetails.setSocialMedia(personalDetails.getSocialMedia());

        MultipartFile profilePic = personalDetails.getProfilePic();
        MultipartFile cv = personalDetails.getCv();

        if (profilePic != null && !profilePic.isEmpty()) {
            FileUploadResponse profilePicUploadResponse = fileUploadService.uploadFile(profilePic);
            newPersonalDetails.setProfilePic(profilePicUploadResponse.getFilePath());
        }

        if (cv != null && !cv.isEmpty()) {
            FileUploadResponse cvUploadResponse = fileUploadService.uploadFile(cv);
            newPersonalDetails.setCv(cvUploadResponse.getFilePath());
        }

        return repository.save(newPersonalDetails);
    }

    public Optional<PersonalDetails> findById(UUID id) {
        return repository.findById(id);
    }

    public List<PersonalDetails> findAll() {
        return repository.findAll();
    }

    public PersonalDetails update(PersonalDetails personalDetails) {
        return repository.save(personalDetails);
    }


    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
