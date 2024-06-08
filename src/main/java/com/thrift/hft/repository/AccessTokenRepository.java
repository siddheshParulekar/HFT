package com.thrift.hft.repository;


import com.thrift.hft.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {


    Optional<AccessToken> findByTokenAndIsValid(String token,Boolean isValid);
}
