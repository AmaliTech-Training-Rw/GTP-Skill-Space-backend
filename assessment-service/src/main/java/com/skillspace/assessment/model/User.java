package com.skillspace.assessment.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Table("users")
public class User {

    @PrimaryKey
    private UUID id;

    private String username;
    private String email;

    @CassandraType(type = CassandraType.Name.SET, typeArguments = CassandraType.Name.TEXT)
    private Set<String> earnedBadges = new HashSet<>();

    // Method to check if the user has a specific badge
    public boolean hasBadge(String badgeName) {
        return earnedBadges.contains(badgeName);
    }

    // Add other relevant getters and setters
}
