package com.skillspace.career.Service;

import com.skillspace.career.Model.Career;
import com.skillspace.career.Repository.CareerRepository;
import com.skillspace.career.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Service
public class CareerService {

    private final CareerRepository careerRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CareerService(CareerRepository careerRepository, RestTemplate restTemplate) {
        this.careerRepository = careerRepository;
        this.restTemplate = restTemplate;
    }

    // Method to check if a company exists by companyId
    private boolean doesCompanyExist(UUID companyId) {
        String url = String.format("http://user-management-service/api/users/companies/id/%s", companyId);
        ResponseEntity<CompanyDTO> response = restTemplate.getForEntity(url, CompanyDTO.class);

        // Check if the response is successful and the body is not null
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }

    public Career createCareer(Career career) {
        // Validate companyId before creating the career
        if (career.getCompanyId() == null || !doesCompanyExist(career.getCompanyId())) {
            throw new RuntimeException("Company with ID " + career.getCompanyId() + " does not exist");
        }

        // Generate a unique ID if null
        if (career.getId() == null) {
            career.setId(UUID.randomUUID()); // Generates a unique ID if null
        }

        return careerRepository.save(career);
    }

    public List<Career> getProgramsByCompanyName(String companyName) {
        // Step 1: Fetch companyId by companyName from user management service
        String url = String.format("http://user-management-service/api/users/companies/%s", companyName);
        ResponseEntity<CompanyDTO> response = restTemplate.getForEntity(url, CompanyDTO.class);

        // Step 2: Check if response is successful and get companyId
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            UUID companyId = response.getBody().getCompanyId();

            // Step 3: Fetch all Career programs by companyId
            return fetchProgramsByCompanyId(companyId);
        } else {
            throw new RuntimeException("Company not found or error occurred in user management service");
        }
    }

    private List<Career> fetchProgramsByCompanyId(UUID companyId) {
        return careerRepository.findByCompanyId(companyId);
    }

    public Career updateCareer(UUID id, Career careerDetails) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found"));

        career.setName(careerDetails.getName());
        career.setDescription(careerDetails.getDescription());
        career.setStartDate(careerDetails.getStartDate());
        career.setEndDate(careerDetails.getEndDate());
        career.setStatus(careerDetails.getStatus());
        career.setRequirements(careerDetails.getRequirements());
        career.setRequiredBadges(careerDetails.getRequiredBadges());
        career.setOptionalBadges(careerDetails.getOptionalBadges());

        return careerRepository.save(career);
    }

    public List<Career> getPublishedCareers() {
        return careerRepository.findByStatus("published");
    }

    public List<Career> getAllPrograms() {
        return careerRepository.findAll();
    }

    public Career saveAsDraft(Career career) {
        career.setStatus("draft");
        return careerRepository.save(career);
    }

    public Career saveAsPublished(Career career) {
        career.setStatus("published");
        return careerRepository.save(career);
    }

    public List<Career> getDraftCareers() {
        return careerRepository.findByStatus("draft");
    }

    public void deleteCareer(UUID id) {
        careerRepository.deleteById(id);
    }
}
