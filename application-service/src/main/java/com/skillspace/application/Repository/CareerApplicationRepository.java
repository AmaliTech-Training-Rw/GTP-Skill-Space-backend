package com.skillspace.application.Repository;

import com.skillspace.application.Model.CareerApplication;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface CareerApplicationRepository extends CassandraRepository<CareerApplication, Long> {
}
