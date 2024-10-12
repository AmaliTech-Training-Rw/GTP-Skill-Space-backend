package com.skillspace.assessment.service;

import com.skillspace.assessment.model.User;
import com.skillspace.assessment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Set<String> getEarnedBadges(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getEarnedBadges).orElse(null);
    }
}