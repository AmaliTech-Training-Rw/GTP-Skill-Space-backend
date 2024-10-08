package com.skillspace.user.controllers;

import com.skillspace.user.dto.PersonalDetailsDto;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.service.PersonalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal-details")
public class PersonalDetailsController {

    private final PersonalDetailsService service;

    @Autowired
    public PersonalDetailsController(PersonalDetailsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PersonalDetails> create(@RequestBody PersonalDetailsDto personalDetails) {
        return ResponseEntity.ok(service.create(personalDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDetails> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PersonalDetails>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalDetails> update(@PathVariable UUID id, @RequestBody PersonalDetails personalDetails) {
        if (!id.equals(personalDetails.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.update(personalDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
