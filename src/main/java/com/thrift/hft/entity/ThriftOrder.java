package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import com.thrift.hft.enums.DeliveryStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class ThriftOrder extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(unique = true)
    String id;
    String cartId;
    String addressId;
    BigDecimal amount;
    String orderStatus;
    String razorpayOrderId;
    Long userId;
    String transactionId;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    DeliveryStatus deliveryStatus = DeliveryStatus.PROCESSING;

}
