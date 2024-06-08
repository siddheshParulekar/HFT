package com.thrift.hft.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 255)
    String firstname;
    @Column(length = 255)
    String lastname;
    Long mobileNumber;
    String countryCode;
    String email;
    String location;
    @Column(length = 255)
    String username;
    String password;
    Boolean isActive = Boolean.TRUE;
}
