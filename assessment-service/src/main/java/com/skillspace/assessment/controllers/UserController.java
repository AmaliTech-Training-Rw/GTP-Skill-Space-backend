package com.skillspace.assessment.controllers;

import com.skillspace.assessment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get earned badges for a user
    @GetMapping("/{id}/badges")
    public ResponseEntity<Set<String>> getUserBadges(@PathVariable UUID id) {
        Set<String> badges = userService.getEarnedBadges(id);
        if (badges != null) {
            return ResponseEntity.ok(badges);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

