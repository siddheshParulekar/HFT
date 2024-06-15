package com.thrift.hft.dto;

import com.thrift.hft.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;
    String firstname;
    String lastname;
    Long mobileNumber;
    String email;
    Role role;
    Boolean isActive;
    String address;
}
