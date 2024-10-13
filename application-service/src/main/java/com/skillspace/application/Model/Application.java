package com.skillspace.application.Model;

import com.skillspace.application.util.IdGenerator;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("career_applications")
public class Application {
    @PrimaryKey
    private Long id = IdGenerator.generateId();
    private Long careerId;
    private Long talentId;
    private String status = "pending"; // e.g., "pending", "approved", "rejected"
    private LocalDateTime commencementDate;
}

