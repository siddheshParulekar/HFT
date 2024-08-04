package com.thrift.hft.entity;


import com.thrift.hft.audit.Auditable;
import com.thrift.hft.dto.AddressDTO;
import com.thrift.hft.enums.AddressType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class Address extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
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


    public AddressDTO getAddressDTO(){
        return new AddressDTO(userName,houseNumber,streetAddress,landmark,city,state,pinCode,country,addressType,userId);
    }
}
