package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessToken extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "token", length = 500)
    String token;

    Long userId;

    Boolean isValid = Boolean.TRUE;

    LocalDateTime logoutAt;

    public AccessToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public AccessToken(String token, Boolean isValid) {
        this.token = token;
        this.isValid = isValid;
    }
}
