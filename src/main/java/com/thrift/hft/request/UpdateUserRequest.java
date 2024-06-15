package com.thrift.hft.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {


    @Pattern(regexp = "^[6789]\\d{9}$", message = "Mobile number should contain 10 digits")
    String mobileNumber;
    String address;
    @Pattern(regexp = "^\\w+([-+.']\\w+)*@[A-Za-z0-9]+([-.][A-Za-z0-9]+)*\\.\\w+([-.]\\w+)*$", message = "Enter valid Email-Id")
    String email;

}
