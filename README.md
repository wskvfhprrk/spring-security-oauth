# spring-security-oauth
oauth服务的使用——oauth2如何保护微服务的

学习来源于youtube的How to Secure Micro services - oAuth2 

## 1、建立authorizationServer服务

### 1、建立maven项目

需要的denpendencies：

- spring web
- spring data jpa

### 2、把项目配置为authorization server

- 可以直接在main上加入注解`@EanbleAuthorizationServer`
- 可以写一个AuthorizationServerConfig:

```java
/**
 * 配置启动为authorizationServer服务
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig {
}
```

###  3、配置`application.yml`：

```yml

spring:
  security:
    user:
      name: foo
      password: 123456
      roles: admin,user
security:
  oauth2:
    client:
      client-id: client
      client-secret: secret
      authorized-grant-types: password,refresh_token,authorization_code,client_credentails
      scope: w,r
      access-token-validity-seconds: 3600
    authorization:
      check-token-access: permitAll

```

### 5、使用postman测试：

- 访问/oauth/token获取到token
- 访问/oauth/check_token可以拿到token的值
**要配置check-token-access: permitAll**

