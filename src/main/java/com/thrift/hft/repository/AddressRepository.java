package com.thrift.hft.repository;

import com.thrift.hft.entity.Address;
import com.thrift.hft.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {


    @Modifying
    @Transactional
    void deleteByAddressTypeAndUserId(AddressType addressType, Long userId);
}
