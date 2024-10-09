package com.skillspace.user.service;

import com.skillspace.user.dto.EducationDto;
import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
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

    public Education updateEducationDetails(EducationDto educationDto) {
        Optional<Education> isDetailsExist = educationRepository.findEducationByTalentId(educationDto.getTalentId());

        if (isDetailsExist.isPresent()) {
            Education education = isDetailsExist.get();
//            Education education = new Education();
//            education.setId(UUID.randomUUID());
            Optional.ofNullable(educationDto.getTalentId()).ifPresent(education::setTalentId);
            Optional.ofNullable(educationDto.getNameOfInstitution()).ifPresent(education::setNameOfInstitution);
            Optional.ofNullable(educationDto.getAddressOfInstitution()).ifPresent(education::setAddressOfInstitution);
            Optional.ofNullable(educationDto.getCountry()).ifPresent(education::setCountry);
            Optional.ofNullable(educationDto.getNameOfProgram()).ifPresent(education::setNameOfProgram);
            Optional.ofNullable(educationDto.getProgramStatus()).ifPresent(education::setProgramStatus);
            Optional.ofNullable(educationDto.getDateCommencement()).ifPresent(education::setDateCommencement);
            Optional.ofNullable(educationDto.getDateCompleted()).ifPresent(education::setDateCompleted);

            MultipartFile transcript = educationDto.getTranscripts();

            if (transcript != null && !transcript.isEmpty()) {
                FileUploadResponse transcriptsResponse = fileUploadService.uploadFile(transcript);
                education.setTranscripts(transcriptsResponse.getFilePath());
            }

            return educationRepository.save(education);
        }
        return null;
    }
}
