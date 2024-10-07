package com.skillspace.application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/applications/test")
    public String getUsers() {
        return "List of applications yes";
    }

}
