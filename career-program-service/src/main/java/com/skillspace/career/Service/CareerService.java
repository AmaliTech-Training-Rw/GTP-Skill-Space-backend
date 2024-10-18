package com.skillspace.career.Service;

import com.skillspace.career.Model.Career;
import com.skillspace.career.Repository.CareerRepository;
import com.skillspace.career.dto.CareerProgramRequest;
import com.skillspace.career.dto.CompanyDTO;
import feign.FeignException;
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


@Service
public class CareerService {

    @Autowired
    private final CompanyClient companyClient;
    private final CareerRepository careerRepository;



    @Autowired
    public CareerService(CareerRepository careerRepository,  CompanyClient companyClient) {
        this.careerRepository = careerRepository;

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


    public Career saveCareerProgramAsDraft(CareerProgramRequest request) {
        validateRequest(request);
        verifyCompany(request.getCompanyId());
        Career career = request.getCareer();
        career.setCompanyId(request.getCompanyId());
        career.setStatus("draft");
        return careerRepository.save(career);
    }

    public Career saveCareerProgramAsPublished(CareerProgramRequest request) {
        validateRequest(request);
        verifyCompany(request.getCompanyId());
        Career career = request.getCareer();
        career.setCompanyId(request.getCompanyId());
        career.setStatus("published");
        return careerRepository.save(career);
    }

    private void validateRequest(CareerProgramRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be null");
        }
        if (request.getCareer() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career object cannot be null");
        }
        if (request.getCompanyId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company ID cannot be null");
        }
    }

    private void verifyCompany(Long companyId) {
        try {
            CompanyDTO company = companyClient.getCompanyById(companyId);
            if (company == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + companyId);
            }
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error verifying company: " + e.getMessage());
        }
    }

}
