package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BatchDetails extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal noOfArticle = BigDecimal.ZERO;
    BigDecimal approved = BigDecimal.ZERO;
    BigDecimal rejected = BigDecimal.ZERO;
    Long userId;
    Long mobileNumber;

    public BatchDetails(BigDecimal noOfArticle,Long mobileNumber, Long userId) {
        this.noOfArticle = noOfArticle;
        this.mobileNumber = mobileNumber;
        this.userId = userId;
    }
}
