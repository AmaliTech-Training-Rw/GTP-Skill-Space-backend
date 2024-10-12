package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyDTO;
import com.skillspace.user.entity.Company;
import com.skillspace.user.service.CompanyService;
import com.skillspace.user.service.KafkaProducerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/companies")
public class AdminCompanyApprovalsController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // Get all companies with PENDING status
    @GetMapping("/approvals")
    public List<CompanyDTO> getPendingCompanies() {
        try {
            List<Company> companies = companyService.getPendingCompanies();
            return companies.stream()
                    .map(company -> new CompanyDTO(
                            company.getCompanyId(),
                            company.getUserId().getId(),
                            company.getName(),
                            company.getCertificate(),
                            company.getLogo(),
                            company.getWebsite(),
                            company.getCreatedAt(),
                            company.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching pending companies: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // method to approve a company
    @PostMapping("/{companyId}/approve")
    public ResponseEntity<String> approveCompany(@PathVariable Long companyId) {
        try {
            Company approvedCompany = companyService.approveCompany(companyId);
            kafkaProducerService.sendCompanyApprovalStatusEvent(approvedCompany, "APPROVED");
            return ResponseEntity.ok("Company approved successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while approving the company.");
        }
    }

    // method to reject a company
    @PostMapping("/{companyId}/reject")
    public ResponseEntity<String> rejectCompany(@PathVariable Long companyId) {
        try {
            Company rejectedCompany = companyService.rejectCompany(companyId);
            kafkaProducerService.sendCompanyApprovalStatusEvent(rejectedCompany, "REJECTED");
            return ResponseEntity.ok("Company rejected successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while rejecting the company.");
        }
    }
}

