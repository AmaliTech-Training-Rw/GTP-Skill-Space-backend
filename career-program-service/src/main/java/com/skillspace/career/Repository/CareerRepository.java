package com.skillspace.career.Repository;

import com.skillspace.career.Model.Career;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;



public interface CareerRepository extends CassandraRepository<Career, UUID> {
    List<Career> findByStatus(String status); // For filtering published/draft programs
    List<Career> findByCompanyId(UUID companyId);
    List<Career> findByRequiredBadgesIn(List<String> badges);
}
