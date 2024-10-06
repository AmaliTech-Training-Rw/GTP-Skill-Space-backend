package com.skillspace.user.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/users/all")
    public String getUsers() {
        return "List of users";
    }

}
