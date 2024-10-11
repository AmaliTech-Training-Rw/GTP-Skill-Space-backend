package com.skillspace.user.util.helper;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.dto.UpdateCompany;
import com.skillspace.user.entity.*;
import com.skillspace.user.util.EnumUtil;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HelperMethods {
    private EnumUtil enumUtil = new EnumUtil();

    public PersonalDetailsDto buildPersonalDetailsDto(Long talentId, String firstName, String lastName, String contact,
                                                      String location, boolean available, String badges, String notificationPreference,
                                                      String portfolio, LocalDate dob, String bio, MultipartFile profilePic,
                                                      Map<String, String> socialMedia, MultipartFile cv) {

        PersonalDetailsDto personalDetails = new PersonalDetailsDto();

        personalDetails.setFirstName(firstName);
        personalDetails.setLastName(lastName);
        personalDetails.setContact(contact);
        personalDetails.setLocation(location);
        personalDetails.setAvailable(available);
        personalDetails.setBadges(badges);

        if (enumUtil.isValidEnum(NotificationPreference.class, notificationPreference)) {
            System.out.println(notificationPreference);
            personalDetails.setNotificationPreference(NotificationPreference.valueOf(notificationPreference));
        }

        personalDetails.setPortfolio(portfolio);
        personalDetails.setCv(cv);
        personalDetails.setProfilePic(profilePic);
        personalDetails.setSocialMedia(socialMedia);
        personalDetails.setBio(bio);
        personalDetails.setDob(dob);

        return personalDetails;
    }

    public EducationDto buildEducationDto(Long talentId, String nameOfInstitution,
                                          String addressOfInstitution, String country,
                                          String nameOfProgram, String programStatus, LocalDate dateCommencement,
                                          LocalDate dateCompleted, MultipartFile transcripts) {
        EducationDto educationDto = new EducationDto();
        educationDto.setTalentId(talentId);
        educationDto.setNameOfInstitution(nameOfInstitution);
        educationDto.setAddressOfInstitution(addressOfInstitution);
        educationDto.setCountry(country);
        educationDto.setNameOfProgram(nameOfProgram);

        if (enumUtil.isValidEnum(ProgramStatus.class, programStatus)) {
            educationDto.setProgramStatus(ProgramStatus.valueOf(programStatus));
        }

        educationDto.setDateCommencement(dateCommencement);
        educationDto.setDateCompleted(dateCompleted);
        educationDto.setTranscripts(transcripts);

        return  educationDto;
    }

    public Education buildEducation(EducationDto educationDto) {
        Education education = new Education();
        education.setId(UUID.randomUUID());
        education.setTalentId(educationDto.getTalentId());
        education.setNameOfInstitution(educationDto.getNameOfInstitution());
        education.setAddressOfInstitution(educationDto.getAddressOfInstitution());
        education.setCountry(educationDto.getCountry());
        education.setNameOfProgram(educationDto.getNameOfProgram());
        education.setProgramStatus(educationDto.getProgramStatus());
        education.setDateCommencement(educationDto.getDateCommencement());
        education.setDateCompleted(educationDto.getDateCompleted());

        return education;
    }

    public void updateEducationFields(Education education, EducationDto educationDto) {
        Optional.ofNullable(educationDto.getTalentId()).ifPresent(education::setTalentId);
        Optional.ofNullable(educationDto.getNameOfInstitution()).ifPresent(education::setNameOfInstitution);
        Optional.ofNullable(educationDto.getAddressOfInstitution()).ifPresent(education::setAddressOfInstitution);
        Optional.ofNullable(educationDto.getCountry()).ifPresent(education::setCountry);
        Optional.ofNullable(educationDto.getNameOfProgram()).ifPresent(education::setNameOfProgram);
        Optional.ofNullable(educationDto.getProgramStatus()).ifPresent(education::setProgramStatus);
        Optional.ofNullable(educationDto.getDateCommencement()).ifPresent(education::setDateCommencement);
        Optional.ofNullable(educationDto.getDateCompleted()).ifPresent(education::setDateCompleted);
    }
}
