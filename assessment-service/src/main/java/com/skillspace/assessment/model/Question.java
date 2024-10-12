package com.skillspace.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UserDefinedType("question")
public class Question {

        @CassandraType(type = CassandraType.Name.TEXT)
        private String question_text;

        @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
        private List<String> options;

        @CassandraType(type = CassandraType.Name.TEXT)
        private String correct_answer;

        @CassandraType(type = CassandraType.Name.INT)
        private int points;

        @CassandraType(type = CassandraType.Name.TEXT)
        private String imageUrl;
}
