package com.thrift.hft.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @Pattern(regexp = "^\\w+([-+.']\\w+)*@[A-Za-z0-9]+([-.][A-Za-z0-9]+)*\\.\\w+([-.]\\w+)*$", message = "Incorrect Email ID Please use registered email id only")
    @NotEmpty(message = "Incorrect Email ID Please use registered email id only")
    String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=*_])(?=\\S+$).{8,16}$",
            message = "Please enter correct Password")
    @NotEmpty(message = "Please enter correct Password")
    String password;
}

