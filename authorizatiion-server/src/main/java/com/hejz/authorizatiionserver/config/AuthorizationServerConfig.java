package com.hejz.authorizatiionserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 配置启动为authorizationServer服务
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig {
}
