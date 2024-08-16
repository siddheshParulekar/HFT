package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import com.thrift.hft.dto.UserDTO;
import com.thrift.hft.enums.Gender;
import com.thrift.hft.enums.Role;
import com.thrift.hft.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@Entity
@Builder
public class User extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 255)
    String name;
    Long mobileNumber;
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @Builder.Default
    Boolean isActive = Boolean.TRUE;
    @Builder.Default
    Gender gender = Gender.NA;


    public UserDTO getUserDTO() {
        return new UserDTO(id, name,  mobileNumber, email , role,isActive, gender,CommonUtils.getUserAddress(id));
    }
}
