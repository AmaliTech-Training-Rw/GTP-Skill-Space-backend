package com.skillspace.career.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table("careers")
public class Career {

    @PrimaryKey
    private UUID id;

    private String name;
    private String description;
    private String status; // Can be "draft" or "published"
    private String startDate;
    private String endDate;
    private List<String> requirements;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> requiredBadges;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> optionalBadges;

    public Career() {
        this.id = UUID.randomUUID(); // Automatically generates a unique ID
    }

}
