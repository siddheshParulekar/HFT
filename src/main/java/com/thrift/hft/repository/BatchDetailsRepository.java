package com.thrift.hft.repository;

import com.thrift.hft.entity.BatchDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchDetailsRepository extends JpaRepository<BatchDetails,Long> {
}
