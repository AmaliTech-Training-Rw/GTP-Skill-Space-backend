package com.skillspace.user.controller;

import com.skillspace.user.dto.AdminRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Admin;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/register")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin")
    public ResponseEntity<Admin> registerAdmin(@RequestBody AdminRegistrationRequest request) {
        Account account = createAccountFromRequest(request);
        Admin admin = createAdminFromRequest(request);

        Admin registeredAdmin = adminService.registerUser(admin, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredAdmin);
    }

    // Helper method to create Account from AdminRegistrationRequest
    private Account createAccountFromRequest(AdminRegistrationRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setContact(request.getContact());
        account.setRole(UserRole.ADMIN);
        return account;
    }

    // Helper method to create Admin from AdminRegistrationRequest
    private Admin createAdminFromRequest(AdminRegistrationRequest request) {
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setContact(request.getContact());
        return admin;
    }
}


