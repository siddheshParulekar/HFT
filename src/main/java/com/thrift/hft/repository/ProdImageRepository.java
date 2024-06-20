package com.thrift.hft.repository;

import com.thrift.hft.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdImageRepository extends JpaRepository<ProductImage,String> {
}
