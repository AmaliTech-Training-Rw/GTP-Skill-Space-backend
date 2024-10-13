package com.skillspace.application.Controller;

import com.skillspace.application.Model.Application;
import com.skillspace.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Create a new application
    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        Application createdApplication = applicationService.createApplication(application);
        return new ResponseEntity<>(createdApplication, HttpStatus.CREATED);
    }

    // Get all applications
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Optional<Application> application = applicationService.getApplicationById(id);
        return application.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update an existing application
    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application application) {
        Optional<Application> existingApplication = applicationService.getApplicationById(id);
        if (existingApplication.isPresent()) {
            application.setId(id); // Ensure the ID remains consistent during the update
            Application updatedApplication = applicationService.updateApplication(application);
            return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete an application by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        Optional<Application> application = applicationService.getApplicationById(id);
        if (application.isPresent()) {
            applicationService.deleteApplication(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get applications by career ID
    @GetMapping("/career/{careerId}")
    public ResponseEntity<List<Application>> getApplicationsByCareerId(@PathVariable Long careerId) {
        List<Application> applications = applicationService.getApplicationsByCareerId(careerId);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    // Get applications by talent ID
    @GetMapping("/talent/{talentId}")
    public ResponseEntity<List<Application>> getApplicationsByTalentId(@PathVariable Long talentId) {
        List<Application> applications = applicationService.getApplicationsByTalentId(talentId);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    // Get applications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Application>> getApplicationsByStatus(@PathVariable String status) {
        List<Application> applications = applicationService.getApplicationsByStatus(status);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Application> approveApplication(@PathVariable Long id) {
        try {
            Application approvedApplication = applicationService.approveApplication(id);
            return new ResponseEntity<>(approvedApplication, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Reject an application
    @PutMapping("/{id}/reject")
    public ResponseEntity<Application> rejectApplication(@PathVariable Long id) {
        try {
            Application rejectedApplication = applicationService.rejectApplication(id);
            return new ResponseEntity<>(rejectedApplication, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

