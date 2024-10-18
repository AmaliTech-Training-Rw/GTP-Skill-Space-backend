package com.skillspace.application.Model;

import com.skillspace.application.util.IdGenerator;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("career_applications")
public class Application {
    @PrimaryKey
    private Long id = IdGenerator.generateId();
    private Long careerId;
    private Long talentId;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private LocalDateTime commencementDate;
}
