package com.skillspace.career.Client;


import com.skillspace.career.dto.CompanyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-management-service")
public interface CompanyClient {

    @GetMapping("/auth/register/{name}")
     CompanyDTO getCompanyByName(@PathVariable("name") String name);
}
