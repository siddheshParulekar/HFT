package com.thrift.hft.repository;

import com.thrift.hft.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdImageRepository extends JpaRepository<ProductImage,String> {

    List<ProductImage> findByProductId(String productId);

    @Query("SELECT pi FROM ProductImage pi JOIN pi.product p WHERE p.sellerId = :sellerId")
    List<ProductImage> findBySellerId(@Param("sellerId") Long sellerId);


}
