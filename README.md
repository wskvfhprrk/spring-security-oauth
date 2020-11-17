# spring-security-oauth
oauth服务的使用——oauth2如何保护微服务的

学习来源于youtube的[Tutorial] Spring OAuth2 - the easy way

## 1、建立authorizationServer服务

### 1、建立maven项目

需要的denpendencies：

- spring web
- spring data jpa
- cloud oauth2
- h2

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

### 3、建立用户的实体类和Reposiatory：

```java

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String username, password;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
```

### 4、配置`JpaConfig`

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.hejz.authorizatiionserver.repository")
public class JpaConfig {
}
```

### 5、`UserDetailsService`实现类：

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findByUsername(username);
        return user.map(account -> new User(account.getUsername(), account.getPassword(),
                AuthorityUtils.createAuthorityList("write", "read")))
                //用户不存在抛异常
                .orElseThrow(() -> new UsernameNotFoundException("该用户不存在"));
    }
}
```

### 7、spring security配置类:

```java
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

**注意：AuthenticationManager的Bean,方法名一定要写成authenticationManagerBean否则会报堆栈溢出（o.s.s.o.provider.endpoint.TokenEndpoint  : Handling error: NestedServletException, Handler dispatch failed; nested exception is java.lang.StackOverflowError）**

### 8、oauth2配置类：

```java
@Configuration
public class OauthConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * /auth/check_token它是通过端点或检查令牌来执行此操作
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许身份验证通过资源服务器
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //内存里建立一个clientId为client，密钥为secret,scopes为all的客户端
        clients.inMemory()
                .withClient("client")
                .secret(passwordEncoder.encode("secret"))
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TokenStore tokenStore(){
        //使用内存
        return new InMemoryTokenStore();
    }
}
```

### 9、项目启动后添加一个用户到数据库中：

```java
@SpringBootApplication
public class AuthorizatiionServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizatiionServerApplication.class, args);
    }

    @Autowired
    AccountRepository accountRepository;

//    @PostConstruct
//    public void init() {
//        //启动后可以加载用户到数据库中
//        accountRepository.deleteAll();//先清空数据
//        accountRepository.save(new Account("foo", "pass"));
//        System.out.println("数据加载完毕");
//    }
    @Override
	public void run(String... args) throws Exception {
		//启动后可以加载用户到数据库中
		accountRepository.deleteAll();//先清空数据
		accountRepository.save(new Account("foo","pass"));
		System.out.println("数据加载完毕");
	}
}
```

**可以使用@PostConstruct注解，或是在Application中实现commandLineRunner,main主程运行完后在run方法中加载程序运行加入预置数据即可**

### 10、使用postman测试

- 使用basic auth方式，**username和password要与oauth配置中的clinet中的一致，params中username和password要与数据库中的用户名密码一致**，同时grant_type必须为：password——**在微服务环境中，服务间的访问一般使用possword方式，对外服务则使用code方式获取access_token**，使用方法为post，地址`/oauth/token`即可获取token。



### 11、修改代码，使其符合开发和生产环境

#### 1、用户信息增加一些字段，修改Account:

```java
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

```

#### 2、修改`UserDetailsServiceImpl`：

```java
@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findByUsername(username);
        return user.map(account ->
                new User(account.getUsername(), account.getPassword(), account.getEnable(), account.getExpired(), account.getCredentials(), account.getLock()
                        , Arrays.stream(account.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
        )
                //用户不存在抛异常
                .orElseThrow(() -> new UsernameNotFoundException("该用户不存在"));
    }
```

#### 3、初始化数据修改：

```java
accountRepository.save(new Account("foo","pass",true,true,true,true, "write,read"));
```

## 2、建resourceServer

### 1、在授权服务的oauth配置文件`OauthConfigurer`中，把clients配置添加入clientId和secert

```java
@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //内存里建立一个clientId为client，密钥为secret,scopes为all的客户端
        clients.inMemory()
                .withClient("client")
                .secret(passwordEncoder.encode("secret"))
                .scopes("all")
                .and()
                .withClient("resource-server").secret(passwordEncoder.encode("resource-secret")).scopes("all");
    }
```

### 2、添加依赖包：

- web
- cloud oauth2

### 3、启动资源服务器——添加注解`@EnableResourceServer`

4、配置`application.yml`:

```yml
server:
  port: 8081

security:
  oauth2:
    resource:
      token-info-uri: http://localhost:8080/oauth/check_token
    client:
      client-id: resource-server
      client-secret: resource-secret
      authorized-grant-types: password
```

5、使用postman测试：

- 直接访问开发权限的可不用认证就访问了
- 如果需要认证的api，需要先向authorizationServer申请token，加入token才可以访问