package com.skillspace.career.Model;

import com.skillspace.career.util.IdGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

//import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("careers")
public class Career {

    @PrimaryKey
    private Long id = IdGenerator.generateId();

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 500)
    private String description;


    private String status; // Can be "draft" or "published"

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String startDate;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String endDate;

    @NotNull
    @Size(min = 1)
    private List<String> requirements;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> requiredBadges;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> optionalBadges;

    @Column("created_at")
    @PastOrPresent
    private LocalDateTime createdAt;

    @Column("updated_at")
    @PastOrPresent
    private LocalDateTime updatedAt;

    @NotNull
    private Long companyId;
}
