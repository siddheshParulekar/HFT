package com.thrift.hft.entity;


import com.thrift.hft.audit.Auditable;
import com.thrift.hft.dto.AddressDTO;
import com.thrift.hft.enums.AddressType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class Address extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(unique = true)
    String id;
    String userName;
    String houseNumber;
    String streetAddress;
    String landmark;
    String city;
    String state;
    String pinCode;
    @Builder.Default
    String country = "India";
    @Enumerated(EnumType.STRING)
    AddressType addressType;
    Long userId;
    Boolean isDefault;


    public AddressDTO getAddressDTO(){
        return new AddressDTO(id,userName,houseNumber,streetAddress,landmark,city,state,pinCode,country,addressType,userId,isDefault);
    }
}
