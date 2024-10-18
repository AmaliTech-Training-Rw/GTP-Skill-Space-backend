package com.skillspace.career.Client;


import com.skillspace.career.dto.CompanyDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@FeignClient(name = "user-management-service")
public interface CompanyClient {

    @GetMapping("/auth/register/company/name/{name}")
     CompanyDTO getCompanyByName(@PathVariable("name") String name);

    @GetMapping("auth/register/company/id/{companyId}")
    CompanyDTO getCompanyById(@PathVariable("companyId") Long companyId);
}
