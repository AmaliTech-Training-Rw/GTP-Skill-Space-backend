package com.skillspace.user.controller;

import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.service.TalentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/register")
@Validated
public class TalentController {

    @Autowired
    private TalentService talentService;

    @PostMapping("/talent")
    public ResponseEntity<?> registerTalent(@Valid @RequestBody TalentRegistrationRequest request) {
        Talent registeredTalent = talentService.registerTalent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredTalent);
    }
}