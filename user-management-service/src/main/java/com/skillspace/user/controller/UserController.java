package com.skillspace.user.controller;

import com.skillspace.user.dto.ChangePasswordDto;
import com.skillspace.user.service.UserService;
import com.skillspace.user.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}/change-password")
    public CustomResponse<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(id, changePasswordDto);
    }
}

