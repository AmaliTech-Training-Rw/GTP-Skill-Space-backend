package com.skillspace.career.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/careers")
    public String getUsers() {
        return "List of careers";
    }

}
