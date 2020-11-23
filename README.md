# spring-security-oauth
oauth服务的使用——oauth2如何保护微服务的

学习来源于youtube的How to Secure Micro services - oAuth2 

## 1、建立简单authorizationServer服务

### 1、建立maven项目

需要的denpendencies：

- spring web
- cloud oauth2

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

## 2、使用代码替代配置文件

### 1、建立`AuthServerConfigurations`：

```java
@Configuration
public class AuthServerConfigrations extends WebSecurityConfigurerAdapter implements AuthorizationServerConfigurer {

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client").secret("secret").scopes("r,w").authorizedGrantTypes("password","refresh_token");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

    }
}
```

**由于java不能双继承，使用实现`AuthorizationServerConfigurer`即可**

### 2、正常可以启动，但获取token报错401，控制台报错：

```linux
There is no PasswordEncoder mapped for the id "null"
```

原因是没有加载`PasswordEncoder`这个bean。

### 3、在`AuthServerConfigurations`添加配置：

```java
	@Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
```

需要实始化用户密码和客户端密钥的地方使用`passwordEncoder.encode`加密