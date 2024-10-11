package com.skillspace.career.Service;

import com.skillspace.career.Model.Career;
import com.skillspace.career.Repository.CareerRepository;
import com.skillspace.career.dto.CompanyDTO;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.skillspace.career.Client.CompanyClient;

import java.util.List;
import java.util.UUID;

@Service
public class CareerService {
    @Autowired
    private final CompanyClient companyClient;
    private final CareerRepository careerRepository;
    private final RestTemplate restTemplate;


    @Autowired
    public CareerService(CareerRepository careerRepository, RestTemplate restTemplate, CompanyClient companyClient) {
        this.careerRepository = careerRepository;
        this.restTemplate = restTemplate;
        this.companyClient = companyClient;
    }

//    // Method to check if a company exists by companyId using RestTemplate
//    private boolean doesCompanyExist(Long companyId) {
//        try {
//            String url = String.format("http://user-management-service/api/users/companies/id/%s", companyId);
//            ResponseEntity<CompanyDTO> response = restTemplate.getForEntity(url, CompanyDTO.class);
//
//            // Check if the response is successful and the body is not null
//            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
//        } catch (Exception e) {
//            // Log the error and return false if the company service call fails
//            System.err.println("Error occurred while checking company existence: " + e.getMessage());
//            return false;
//        }
//    }

//    public Career createCareer(Career career) {
//        // Validate companyId before creating the career
//        if (career.getCompanyId() == null || !doesCompanyExist(career.getCompanyId())) {
//            throw new RuntimeException("Company with ID " + career.getCompanyId() + " does not exist");
//        }
//
//        // Generate a unique Long ID if null
//        if (career.getId() == null) {
//            career.setId(Math.abs(UUID.randomUUID().getMostSignificantBits())); // Generates a unique Long ID
//        }
//
//        return careerRepository.save(career);
//    }

    public Career updateCareer(Long id, Career careerDetails) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + id));

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

    public void deleteCareer(Long id) {
        careerRepository.deleteById(id);
    }

    public List<Career> getProgramsByCompanyId(Long companyId) {
        return careerRepository.findByCompanyId(companyId);
    }

    public List<Career> getProgramsByCompanyName(String companyName) {
        CompanyDTO company = companyClient.getCompanyByName(companyName);
        if (company == null || company.getCompanyId() == null) {
            throw new RuntimeException("Company with name " + companyName + " does not exist");
        }
        return careerRepository.findByCompanyId(company.getCompanyId());
    }

}
