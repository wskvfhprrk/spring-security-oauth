package com.hejz.authenticationserverjdbc.repository;

import com.hejz.authenticationserverjdbc.entiey.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);
}
