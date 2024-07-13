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
    String houseNumber;
    String streetAddress;
    String locality;
    String landmark;
    String city;
    String state;
    String pinCode;
    @Builder.Default
    String country = "India";
    @Enumerated(EnumType.STRING)
    AddressType addressType;
    Long userId;

    public Address(Long userId, AddressType addressType, String country, String pinCode, String state, String city, String landmark, String locality, String streetAddress, String houseNumber) {
        this.userId = userId;
        this.addressType = addressType;
        this.country = country;
        this.pinCode = pinCode;
        this.state = state;
        this.city = city;
        this.landmark = landmark;
        this.locality = locality;
        this.streetAddress = streetAddress;
        this.houseNumber = houseNumber;
    }

    public AddressDTO getAddressDTO(){
        return new AddressDTO(houseNumber,streetAddress,locality,landmark,city,state,pinCode,country,addressType,userId);
    }
}
