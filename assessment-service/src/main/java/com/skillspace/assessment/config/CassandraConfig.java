package com.skillspace.assessment.config;

import com.thoughtworks.xstream.XStream; // Import XStream instead of Mapper
import com.datastax.oss.driver.api.core.session.Session;
import com.skillspace.assessment.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {

    @Autowired
    private Session session;

    @Bean
    public XStream quizMapper() { // Use XStream for mapping
        XStream xStream = new XStream();
        xStream.processAnnotations(Quiz.class); // Process annotations in your Quiz class
        return xStream;
    }
}


