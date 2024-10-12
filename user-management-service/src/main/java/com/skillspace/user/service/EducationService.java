package com.skillspace.user.service;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.dto.UpdateEducationDto;
import com.skillspace.user.entity.Education;
import com.skillspace.user.repository.EducationRepository;
import com.skillspace.user.repository.TalentRepository;
import com.skillspace.user.util.CustomResponse;
import com.skillspace.user.util.helper.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    private final FileUploadService fileUploadService;
    private final TalentRepository talentRepository;

    @Autowired
    public EducationService(EducationRepository educationRepository, FileUploadService fileUploadService, TalentRepository talentRepository) {
        this.educationRepository = educationRepository;
        this.fileUploadService = fileUploadService;
        this.talentRepository = talentRepository;
    }

    public CustomResponse<Education> createEducationDetails(EducationDto educationDto, MultipartFile reqTranscript) {
        try {

            Boolean isTalentExist = talentRepository.existsById(educationDto.getTalentId());

            if (!isTalentExist) {
                return new CustomResponse<>("Talent not found", HttpStatus.NOT_FOUND.value());
            }

            HelperMethods helperMethods = new HelperMethods();
            Education education = helperMethods.buildEducation(educationDto);

            MultipartFile transcript = reqTranscript;

            if (transcript != null && !transcript.isEmpty()) {
                FileUploadResponse transcriptsResponse = fileUploadService.uploadFile(transcript);
                education.setTranscripts(transcriptsResponse.getFilePath());
            }
            Education savedEducation = educationRepository.save(education);

            return new CustomResponse<>("Education details created successfully", HttpStatus.OK.value(), savedEducation);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create education details", e);
        }

    }


    public CustomResponse<Education> updateEducationDetails(UUID id, UpdateEducationDto educationDto, MultipartFile reqTranscript) {

        try {
            Optional<Education> isDetailsExist = educationRepository.findById(id);

            if (isDetailsExist.isPresent()) {
                Education education = isDetailsExist.get();
                new HelperMethods().updateEducationFields(education, educationDto);

                MultipartFile transcript = reqTranscript;

                if (transcript != null && !transcript.isEmpty()) {
                    FileUploadResponse transcriptsResponse = fileUploadService.uploadFile(transcript);
                    education.setTranscripts(transcriptsResponse.getFilePath());
                }
                Education updatedEducation = educationRepository.save(education);
                return new CustomResponse<>("Education details updated successfully", HttpStatus.OK.value(), updatedEducation);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Education record not found");
            }
        } catch (Exception e) {
            return new CustomResponse<>("Failed to update education details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Optional<Education> findTalentEducationRecord(Long talentId) {
        try {
            return educationRepository.findEducationByTalentId(talentId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid talentId provided: " + talentId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Education record found for talentId: " + talentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while retrieving talent education record: " + e.getMessage());
        }
    }

}
