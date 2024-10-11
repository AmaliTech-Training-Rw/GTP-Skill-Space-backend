package com.skillspace.application.service;


import com.skillspace.application.Model.CareerApplication;
import com.skillspace.application.Client.CareerProgramClient;
import com.skillspace.application.Client.UserManagementClient;
import com.skillspace.application.dto.CareerApplicationDTO;
import com.skillspace.application.dto.CareerDTO;
import com.skillspace.application.dto.TalentDTO;
import com.skillspace.application.Repository.CareerApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private CareerApplicationRepository careerApplicationRepository;

    @Autowired
    private UserManagementClient userManagementClient;

    @Autowired
    private CareerProgramClient careerProgramClient;

    public String applyToCareer(CareerApplicationDTO applicationDTO) {
        CareerDTO career = careerProgramClient.getCareerById(applicationDTO.getCareerId());
        TalentDTO talent = userManagementClient.getTalentById(applicationDTO.getTalentId());

        if (talent.getUpdatedAt().isBefore(LocalDateTime.now().minusDays(30))) {
            throw new IllegalStateException("Profile must be updated before applying");
        }

        List<String> talentBadges = userManagementClient.getTalentBadges(applicationDTO.getTalentId());

        if (!talentBadges.containsAll(career.getRequiredBadges())) {
            throw new IllegalStateException("Talent does not have all required badges");
        }

        CareerApplication application = new CareerApplication();
        application.setCareerId(applicationDTO.getCareerId());
        application.setTalentId(applicationDTO.getTalentId());
        application.setStatus("pending");
        application.setAppliedAt(LocalDateTime.now());

        careerApplicationRepository.save(application);

        return "Application submitted successfully";
    }
}

