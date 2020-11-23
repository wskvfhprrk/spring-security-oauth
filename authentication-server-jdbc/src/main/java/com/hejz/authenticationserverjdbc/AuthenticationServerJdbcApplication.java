package com.hejz.authenticationserverjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
@EnableJpaRepositories(basePackages = "com.hejz.authenticationserverjdbc.repository")
public class AuthenticationServerJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServerJdbcApplication.class, args);
	}

}
