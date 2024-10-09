package com.skillspace.career.controllers;

import com.skillspace.career.Model.Career;
import com.skillspace.career.Service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/careers")
public class CareerController {

    private final CareerService careerService;

    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @PostMapping
    public Career createCareer(@RequestBody Career career) {
        return careerService.createCareer(career);
    }

    @PutMapping("/{id}")
    public Career updateCareer(@PathVariable UUID id, @RequestBody Career careerDetails) {
        return careerService.updateCareer(id, careerDetails);
    }

    @GetMapping("all")
    public List<Career> getAllPrograms() {
        return careerService.getAllPrograms();
    }

    @GetMapping("/published")
    public List<Career> getPublishedCareers() {
        return careerService.getPublishedCareers();
    }

    @GetMapping("/draft")
    public List<Career> getDraftCareers() {
        return careerService.getDraftCareers();
    }

    @PostMapping("/draft")
    public ResponseEntity<Career> saveCareerAsDraft(@RequestBody Career career) {
        Career savedDraft = careerService.saveAsDraft(career);
        return new ResponseEntity<>(savedDraft, HttpStatus.CREATED);
    }
    @PostMapping("/published")
    public ResponseEntity<Career> savePublishedCareer(@RequestBody Career career) {
        Career savedPublished = careerService.saveAsPublished(career);
        return new ResponseEntity<>(savedPublished, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteCareer(@PathVariable UUID id) {
        careerService.deleteCareer(id);
    }
}

