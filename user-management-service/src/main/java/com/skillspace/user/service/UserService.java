package com.skillspace.user.service;

import com.skillspace.user.dto.ChangePasswordDto;
import com.skillspace.user.entity.Account;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomResponse<String> changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        try {
            Optional<Account> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                Account user = userOpt.get();

                if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
                    return new CustomResponse<>("Old password is incorrect", HttpStatus.FORBIDDEN.value());
                }

                // Check if the new passwords match
                if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
                    return new CustomResponse<>("New passwords do not match", HttpStatus.BAD_REQUEST.value());
                }

                // Hash the new password
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);

                return new CustomResponse<>("Password updated successfully", HttpStatus.OK.value());
            } else {
                return new CustomResponse<>("User not found", HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            return new CustomResponse<>("Failed to change password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}

