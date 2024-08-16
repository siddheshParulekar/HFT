package com.thrift.hft.dto;

import com.thrift.hft.entity.Address;
import com.thrift.hft.enums.Gender;
import com.thrift.hft.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;
    String name;
    Long mobileNumber;
    String email;
    Role role;
    Boolean isActive;
    Gender gender;
    List<AddressDTO> addressDTOList;
}
