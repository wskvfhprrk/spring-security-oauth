package com.hejz.authorizatiionserver.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String username, password,roles;
    private Boolean enable,credentials,lock,expired;

    public Account() {
    }

    public Account(String username, String password, Boolean enable, Boolean credentials, Boolean lock, Boolean expired, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.enable = enable;
        this.credentials = credentials;
        this.lock = lock;
        this.expired = expired;
    }
}
