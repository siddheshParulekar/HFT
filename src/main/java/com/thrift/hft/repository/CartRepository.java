package com.thrift.hft.repository;

import com.thrift.hft.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {

   Optional<Cart> findByUserIdAndIsOrdered(Long userId, Boolean isOrdered);
}
