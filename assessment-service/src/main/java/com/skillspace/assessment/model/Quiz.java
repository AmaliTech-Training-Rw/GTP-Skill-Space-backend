package com.skillspace.assessment.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("quiz")
public class Quiz {

    @Id
    private UUID id;

    @NotNull
    private String title;

    @NotNull
    private String companyId;

    @Column
    private List<Question> questions;

    private int timeLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull
    private QuizStatus status;

    @NotNull
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean isGlobal;

    @NotNull
    @CassandraType(type = CassandraType.Name.INT)
    private int passingScore;

    private int totalPoints;

    private String imageUrl;
}
