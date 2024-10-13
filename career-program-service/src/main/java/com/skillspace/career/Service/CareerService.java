package com.skillspace.career.Service;

import com.skillspace.career.Model.Career;
import com.skillspace.career.Repository.CareerRepository;
import com.skillspace.career.dto.CompanyDTO;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.skillspace.career.Client.CompanyClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public List<Career> getCareerById(Long id) {
        Optional<Career> careerOptional = careerRepository.findById(id);
        return careerOptional.map(Collections::singletonList).orElse(Collections.emptyList());
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


    private CompanyDTO verifyAndGetCompany(Long companyId) {
        CompanyDTO company = companyClient.getCompanyById(companyId);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + companyId);
        }
        return company;
    }
    public Career saveCareerProgram(Career career, Long companyId) {
        CompanyDTO company = verifyAndGetCompany(companyId);
        career.setCompanyId(company.getCompanyId());
        return careerRepository.save(career);
    }



}
