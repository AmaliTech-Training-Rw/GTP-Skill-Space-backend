package com.skillspace.application.Client;

import com.skillspace.application.dto.TalentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-management-service")
public interface UserManagementClient {
    @GetMapping("/auth/register/talent/{talentId}")
    TalentDTO getTalentById(@PathVariable Long talentId);

}