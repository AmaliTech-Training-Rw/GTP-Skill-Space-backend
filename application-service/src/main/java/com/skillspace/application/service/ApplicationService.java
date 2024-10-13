package com.skillspace.application.service;

import com.skillspace.application.Model.Application;
import com.skillspace.application.Repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
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
            application.setStatus("approved");
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Application not found");
    }

    public Application rejectApplication(Long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus("rejected");
            return applicationRepository.save(application);
        }
        throw new RuntimeException("Application not found");
    }
}
