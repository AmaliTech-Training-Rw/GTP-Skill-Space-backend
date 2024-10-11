package com.skillspace.assessment.repositories;

import com.skillspace.assessment.model.Badge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BadgeRepository extends CrudRepository<Badge, UUID> {
}

