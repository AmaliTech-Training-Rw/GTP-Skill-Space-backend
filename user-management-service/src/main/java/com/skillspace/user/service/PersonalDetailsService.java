package com.skillspace.user.service;

import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.PersonalDetailsRepository;
import com.skillspace.user.repository.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Consumer;

@Service
public class PersonalDetailsService {

    private final PersonalDetailsRepository repository;

    private final FileUploadService fileUploadService;

    private final TalentRepository talentRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public PersonalDetailsService(PersonalDetailsRepository repository, FileUploadService fileUploadService, TalentRepository talentRepository, AccountRepository accountRepository) {
        this.repository = repository;
        this.fileUploadService = fileUploadService;
        this.talentRepository = talentRepository;
        this.accountRepository = accountRepository;
    }


    public PersonalDetails updatePersonalDetails(Long talentId, PersonalDetailsDto personalDetails, MultipartFile reqProfilePic, MultipartFile reqCv) {
        try {
            PersonalDetails personalDetailsEntity = getPersonalDetailsByTalentId(talentId);
            Talent talent = getTalentById(talentId);

            updateContactIfProvided(personalDetails, talent);
            updateTalentFields(personalDetails, talent);
            updatePersonalDetailsFields(personalDetails, personalDetailsEntity);
            uploadFileIfPresent(reqProfilePic, personalDetailsEntity::setProfilePic);
            uploadFileIfPresent(reqCv, personalDetailsEntity::setCv);

            // Save both entities
            talentRepository.save(talent);
            return repository.save(personalDetailsEntity);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating personal details", e);
        }
    }


    private PersonalDetails getPersonalDetailsByTalentId(Long talentId) {
        return repository.findPersonalDetailsByTalentId(talentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid talentId provided: " + talentId));
    }


    private Talent getTalentById(Long talentId) {
        return talentRepository.findById(talentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid talentId provided: " + talentId));
    }


    private void updateContactIfProvided(PersonalDetailsDto personalDetails, Talent talent) {
        if (personalDetails.getContact() != null && !personalDetails.getContact().isEmpty()) {
            accountRepository.findById(talent.getUserId().getId()).ifPresent(account -> {
                account.setContact(personalDetails.getContact());
                accountRepository.save(account);
            });
        }
    }


    private void updateTalentFields(PersonalDetailsDto personalDetails, Talent talent) {
        Optional.ofNullable(personalDetails.getFirstName()).ifPresent(talent::setFirstName);
        Optional.ofNullable(personalDetails.getLastName()).ifPresent(talent::setLastName);
    }


    private void updatePersonalDetailsFields(PersonalDetailsDto personalDetails, PersonalDetails personalDetailsEntity) {
        Optional.ofNullable(personalDetails.getBio()).ifPresent(personalDetailsEntity::setBio);
        Optional.ofNullable(personalDetails.getDob()).ifPresent(personalDetailsEntity::setDob);
        Optional.ofNullable(personalDetails.getBadges())
                .filter(badges -> !badges.isEmpty())
                .ifPresent(badges -> personalDetailsEntity.setBadges(Arrays.asList(badges.split(","))));
        personalDetailsEntity.setAvailable(personalDetails.isAvailable());
        Optional.ofNullable(personalDetails.getLocation()).ifPresent(personalDetailsEntity::setLocation);
        Optional.ofNullable(personalDetails.getNotificationPreference()).ifPresent(personalDetailsEntity::setNotificationPreference);
        Optional.ofNullable(personalDetails.getPortfolio()).ifPresent(personalDetailsEntity::setPortfolio);
        Optional.ofNullable(personalDetails.getSocialMedia()).ifPresent(personalDetailsEntity::setSocialMedia);
    }

    private void uploadFileIfPresent(MultipartFile file, Consumer<String> setFilePathFunction) {
        if (file != null && !file.isEmpty()) {
            FileUploadResponse uploadResponse = fileUploadService.uploadFile(file);
            setFilePathFunction.accept(uploadResponse.getFilePath());
        }
    }


    public Optional<PersonalDetails> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<PersonalDetails>  findTalentProfile(Long talentId) {
        try{
        return repository.findPersonalDetailsByTalentId(talentId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid talentId provided: " + talentId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Profile found for talentId: " + talentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while retrieving talent profile: " + e.getMessage());
        }
    }
}
