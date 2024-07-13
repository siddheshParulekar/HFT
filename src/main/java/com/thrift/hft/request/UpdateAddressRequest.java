package com.thrift.hft.request;

import com.thrift.hft.enums.AddressType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAddressRequest {

    @NotBlank
    String houseNumber;

    @NotBlank
    String streetAddress;

    String locality;

    @NotBlank
    String landmark;

    @NotBlank
    String city;

    @NotBlank
    String state;

    @NotBlank
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Indian PIN code")
    String pinCode;

    @NotBlank
    String country = "India";

    @NotNull
    AddressType addressType;
}
