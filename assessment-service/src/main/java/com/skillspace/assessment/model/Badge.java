package com.skillspace.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("badges")
public class Badge {
    @PrimaryKey
    private UUID id;
    private String name;
}