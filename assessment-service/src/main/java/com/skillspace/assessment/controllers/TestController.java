package com.skillspace.application.controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {

    @GetMapping("/assessments")
    public String getUsers() {
        return "List of assessments";
    }

}
