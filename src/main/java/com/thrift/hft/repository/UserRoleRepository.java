package com.thrift.hft.repository;

import com.thrift.hft.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoles,Long> {
    Optional<UserRoles> findByUserId(Long userId);
}
