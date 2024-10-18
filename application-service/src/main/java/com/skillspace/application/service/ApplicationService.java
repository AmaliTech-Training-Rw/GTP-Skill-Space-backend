package com.skillspace.application.service;

import com.skillspace.application.Client.CareerProgramClient;
import com.skillspace.application.Client.UserManagementClient;
import com.skillspace.application.Model.Application;
import com.skillspace.application.Model.ApplicationStatus;
import com.skillspace.application.Repository.ApplicationRepository;
import com.skillspace.application.dto.CareerDTO;
import com.skillspace.application.dto.TalentDTO;
import com.skillspace.application.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private final ApplicationRepository applicationRepository;
    private final CareerProgramClient careerProgramClient;
    private final UserManagementClient userManagementClient;


    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository,CareerProgramClient careerProgramClient,UserManagementClient userManagementClient) {
        this.applicationRepository = applicationRepository;
        this.careerProgramClient = careerProgramClient;
        this.userManagementClient = userManagementClient;
    }

    // Create or save a new application
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    // Get all applications
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Get an application by its ID
    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    // Update an existing application
    public Application updateApplication(Application application) {
        return applicationRepository.save(application);
    }

    // Delete an application by its ID
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    // Get applications by career ID
    public List<Application> getApplicationsByCareerId(Long careerId) {
        return applicationRepository.findByCareerId(careerId);
    }

    // Get applications by talent ID
    public List<Application> getApplicationsByTalentId(Long talentId) {
        return applicationRepository.findByTalentId(talentId);
    }

    // Get applications by status
    public List<Application> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }
    public Application approveApplication(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus(ApplicationStatus.APPROVED);
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Application not found");
    }

    public Application rejectApplication(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus(ApplicationStatus.REJECTED);
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Application not found");
    }

    public Application applyForCareer(Application application) {
        // Verify Talent ID
        TalentDTO talent = userManagementClient.getTalentById(application.getTalentId());
        if (talent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent not found with id: " + application.getTalentId());
        }

        // Verify Career ID
        CareerDTO career = careerProgramClient.getCareerById(application.getCareerId());
        if (career == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Career not found with id: " + application.getCareerId());
        }

        // Proceed with application logic
        application.setId(IdGenerator.generateId());
        application.setStatus(ApplicationStatus.PENDING); //
        return applicationRepository.save(application);
    }
}
