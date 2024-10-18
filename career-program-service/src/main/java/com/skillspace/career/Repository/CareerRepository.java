package com.skillspace.career.Repository;

import com.skillspace.career.Model.Career;
import org.springframework.data.cassandra.repository.CassandraRepository;


import java.util.List;

public interface CareerRepository extends CassandraRepository<Career, Long> {
    List<Career> findByStatus(String status);
    List<Career> findByCompanyId(Long companyId);
}
