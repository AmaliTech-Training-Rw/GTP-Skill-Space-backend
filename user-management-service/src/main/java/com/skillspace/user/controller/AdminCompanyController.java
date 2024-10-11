package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyDTO;
import com.skillspace.user.entity.Company;
import com.skillspace.user.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/companies")
public class AdminCompanyController {

    @Autowired
    private CompanyService companyService;

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
        companyService.approveCompany(companyId);
        return ResponseEntity.ok("Company approved successfully.");
    }

    // method to reject a company
    @PostMapping("/{companyId}/reject")
    public ResponseEntity<String> rejectCompany(@PathVariable Long companyId) {
        companyService.rejectCompany(companyId);
        return ResponseEntity.ok("Company rejected successfully.");
    }
}

