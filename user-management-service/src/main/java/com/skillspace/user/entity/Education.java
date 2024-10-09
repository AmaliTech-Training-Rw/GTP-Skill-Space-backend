package com.skillspace.user.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("education")
@Setter
@Getter
public class Education {

    @PrimaryKey
    private UUID id;

    @Column("talent_id")
    private Long talentId;

    @Column("transcripts")
    private String transcripts;

    @Column("name_of_institution")
    private String nameOfInstitution;

    @Column("address_of_institution")
    private String addressOfInstitution;

    @Column("country")
    private String country;

    @Column("name_of_program")
    private String nameOfProgram;

    @Column("program_status")
    private String programStatus;

    @Column("date_commencement")
    private LocalDate dateCommencement;

    @Column("date_completed")
    private LocalDate dateCompleted;
}

