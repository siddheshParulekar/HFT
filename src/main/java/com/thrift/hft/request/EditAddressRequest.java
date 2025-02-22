package com.thrift.hft.request;

import com.thrift.hft.enums.AddressType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditAddressRequest {
    String name;
    String houseNumber;
    String streetAddress;
    String landmark;
    String city;
    String state;
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Indian PIN code")
    String pinCode;
    String country = "India";
    AddressType addressType;
}
