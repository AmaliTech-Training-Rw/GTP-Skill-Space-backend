package com.skillspace.application.Model;

import com.skillspace.application.util.IdGenerator;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("career_applications")
public class CareerApplication {
    @PrimaryKey
    private Long id = IdGenerator.generateId();
    private Long careerId;
    private Long talentId;
    private String status; // e.g., "pending", "approved", "rejected"
    private LocalDateTime appliedAt;
}

