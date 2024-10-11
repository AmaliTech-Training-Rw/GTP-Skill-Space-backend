package com.skillspace.application.Client;

import com.skillspace.application.dto.CareerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "career-program-service")
public interface CareerProgramClient {
    @GetMapping("/api/careers/{careerId}")
    CareerDTO getCareerById(@PathVariable Long careerId);
}
