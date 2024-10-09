package com.skillspace.assessment.service;

import com.skillspace.assessment.model.Badge;
import com.skillspace.assessment.model.User;
import com.skillspace.assessment.repositories.UserRepository;
import com.skillspace.assessment.repositories.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;

    public void awardBadge(UUID userId, UUID badgeId) {
        Optional<Badge> badge = badgeRepository.findById(badgeId);
        if (badge.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                User currentUser = user.get();

                if (!currentUser.hasBadge(badge.get().getName())) {
                    currentUser.getEarnedBadges().add(badge.get().getName());
                    userRepository.save(currentUser);
                }
            }
        }
    }
}
