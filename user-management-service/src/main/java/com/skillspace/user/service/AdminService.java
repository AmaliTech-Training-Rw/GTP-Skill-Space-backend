package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Admin;
import com.skillspace.user.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class AdminService extends UserRegistrationService<Admin> {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    protected Admin saveUser(Admin admin, Account savedAccount) {
        // Link the Admin entity to the saved Account entity
        admin.setUserId(savedAccount);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        return adminRepository.save(admin);
    }
}


