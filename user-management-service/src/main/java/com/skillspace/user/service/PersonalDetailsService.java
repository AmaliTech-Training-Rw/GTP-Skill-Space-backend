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
        Optional<PersonalDetails> isDetailsExist = repository.findPersonalDetailsByTalentId(talentId);
        Optional<Talent> talentInfo = talentRepository.findById(talentId);

        if (isDetailsExist.isPresent() && talentInfo.isPresent()) {
            PersonalDetails newPersonalDetails = isDetailsExist.get();
            Talent newTalent = talentInfo.get();
            Optional<Account> talentAccountExisting = accountRepository.findById(newTalent.getUserId().getId());

            if (personalDetails.getContact() != null && !personalDetails.getContact().isEmpty() && talentAccountExisting.isPresent()) {
                Account talentAccount = talentAccountExisting.get();
                talentAccount.setContact(personalDetails.getContact());
                accountRepository.save(talentAccount);
            }

            Optional.ofNullable(personalDetails.getFirstName()).ifPresent(newTalent::setFirstName);
            Optional.ofNullable(personalDetails.getLastName()).ifPresent(newTalent::setLastName);
            Optional.ofNullable(personalDetails.getBio()).ifPresent(newPersonalDetails::setBio);
            Optional.ofNullable(personalDetails.getDob()).ifPresent(newPersonalDetails::setDob);
            Optional.ofNullable(personalDetails.getBadges())
                    .filter(badges -> !badges.isEmpty())
                    .ifPresent(badges -> newPersonalDetails.setBadges(Arrays.asList(badges.split(","))));

            newPersonalDetails.setAvailable(personalDetails.isAvailable());

            Optional.ofNullable(personalDetails.getLocation()).ifPresent(newPersonalDetails::setLocation);
            Optional.ofNullable(personalDetails.getNotificationPreference()).ifPresent(newPersonalDetails::setNotificationPreference);
            Optional.ofNullable(personalDetails.getPortfolio()).ifPresent(newPersonalDetails::setPortfolio);
            Optional.ofNullable(personalDetails.getSocialMedia()).ifPresent(newPersonalDetails::setSocialMedia);

//            MultipartFile profilePic = personalDetails.getProfilePic();
            MultipartFile profilePic = reqProfilePic;
            if (profilePic != null && !profilePic.isEmpty()) {
                FileUploadResponse profilePicUploadResponse = fileUploadService.uploadFile(profilePic);
                newPersonalDetails.setProfilePic(profilePicUploadResponse.getFilePath());
            }

//            MultipartFile cv = personalDetails.getCv();
            MultipartFile cv = reqCv;
            if (cv != null && !cv.isEmpty()) {
                FileUploadResponse cvUploadResponse = fileUploadService.uploadFile(cv);
                newPersonalDetails.setCv(cvUploadResponse.getFilePath());
            }

            talentRepository.save(newTalent);

            return repository.save(newPersonalDetails);
        }

        return null;
    }

    public Optional<PersonalDetails> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<PersonalDetails>  findTalentProfile(Long talentId) {
        return repository.findPersonalDetailsByTalentId(talentId);
    }
}
