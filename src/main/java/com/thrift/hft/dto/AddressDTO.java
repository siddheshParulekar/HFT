package com.thrift.hft.dto;

import com.thrift.hft.enums.AddressType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDTO {

    String userName;
    String houseNumber;
    String streetAddress;
    String landmark;
    String city;
    String state;
    String pinCode;
    String country = "India";
    AddressType addressType;
    Long userId;
}
