package com.thrift.hft.entity;

import com.thrift.hft.dto.UserDTO;
import com.thrift.hft.enums.Role;
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
public class User {

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
    String address;

    public UserDTO getUserDTO() {
        return new UserDTO(id, name,  mobileNumber, email , role,isActive,address);
    }
}
