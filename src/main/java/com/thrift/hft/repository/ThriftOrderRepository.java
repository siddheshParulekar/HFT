package com.thrift.hft.repository;

import com.thrift.hft.entity.ThriftOrder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThriftOrderRepository extends JpaRepository<ThriftOrder,String> , JpaSpecificationExecutor<ThriftOrder> {

    Optional<ThriftOrder> findByRazorpayOrderId(String razorpayOrderId );

    List<ThriftOrder> findByUserIdAndTransactionIdIsNotNull(Long userId);
}
