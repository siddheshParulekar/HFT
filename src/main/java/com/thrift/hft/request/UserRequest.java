package com.thrift.hft.request;

import com.thrift.hft.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @NotEmpty(message = "Enter valid First Name")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Enter valid First Name")
    String firstname;

    @NotEmpty(message = "Enter valid Last Name")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Enter valid Last Name")
    String lastname;

    @NotEmpty(message = "Enter valid Gender")
    @Pattern(regexp = "Male|Female|Others", message = "Invalid Gender")
    String gender;

    @NotNull(message = "Enter valid Mobile Number")
    @Pattern(regexp = "^[6789]\\d{9}$", message = "Mobile number should contain 10 digits")
    String mobileNumber;

    @Pattern(regexp = "^\\w+([-+.']\\w+)*@[A-Za-z0-9]+([-.][A-Za-z0-9]+)*\\.\\w+([-.]\\w+)*$", message = "Enter valid Email-Id")
    @NotEmpty(message = "Enter valid Email-Id")
    String email;

    @NotEmpty(message = "Enter valid Location")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Enter valid Location")
    String location;

    @NotEmpty(message = "Enter valid username")
    String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=*_])(?=\\S+$).{8,16}$",
            message = "Password must contain atleast 1 Uppercase, 1 Lowercase, 1 Special character from (!@#$%^&+=*_) and 1 Digit. Password size must be min 8 and max 16.")
    @NotEmpty(message = "Enter valid Password")
    String password;

    @NotNull
    Role role;
}

