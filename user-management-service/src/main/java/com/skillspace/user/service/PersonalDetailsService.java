package com.skillspace.user.service;

import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.repository.PersonalDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonalDetailsService {

    private final PersonalDetailsRepository repository;

    @Autowired
    public PersonalDetailsService(PersonalDetailsRepository repository) {
        this.repository = repository;
    }

    public PersonalDetails create(PersonalDetails personalDetails) {
        return repository.save(personalDetails);
    }

    public Optional<PersonalDetails> findById(UUID id) {
        return repository.findById(id);
    }

    public List<PersonalDetails> findAll() {
        return repository.findAll();
    }

    public PersonalDetails update(PersonalDetails personalDetails) {
        return repository.save(personalDetails);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
