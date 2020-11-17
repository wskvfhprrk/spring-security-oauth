package com.hejz.authorizatiionserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.hejz.authorizatiionserver.repository")
public class JpaConfig {
}
