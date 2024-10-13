package com.skillspace.application.Repository;

import com.skillspace.application.Model.Application;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface ApplicationRepository extends CassandraRepository<Application, Long> {
    List<Application> findByCareerId(Long careerId);
    List<Application> findByTalentId(Long talentId);
    List<Application> findByStatus(String status);
}
