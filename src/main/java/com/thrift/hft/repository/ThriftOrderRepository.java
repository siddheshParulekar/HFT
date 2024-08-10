package com.thrift.hft.repository;

import com.thrift.hft.entity.ThriftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThriftOrderRepository extends JpaRepository<ThriftOrder,String> {
}
