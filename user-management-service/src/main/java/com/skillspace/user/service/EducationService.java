package com.skillspace.user.service;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.entity.Education;
import com.skillspace.user.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    private final FileUploadService fileUploadService;

    @Autowired
    public EducationService(EducationRepository educationRepository, FileUploadService fileUploadService) {
        this.educationRepository = educationRepository;
        this.fileUploadService = fileUploadService;
    }

    public Education createEducation(EducationDto educationDto) {
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

        MultipartFile transcript = educationDto.getTranscripts();

        if (transcript != null && !transcript.isEmpty()) {
            FileUploadResponse transcriptsResponse = fileUploadService.uploadFile(transcript);
            education.setTranscripts(transcriptsResponse.getFilePath());
        }

        return educationRepository.save(education);
    }
}
