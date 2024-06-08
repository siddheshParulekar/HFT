package com.thrift.hft;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenRequest {
    Long userId;
    Long userRoleId;
    String email;
    String authority; //RoleName
    String name;
    String username;
    Long roleId;
    String userType;
}
