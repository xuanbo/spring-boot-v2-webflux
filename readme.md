# spring-boot-v2之webflux篇

> 介绍spring-boot-**v2**中**webflux**。
> 基于2.0.5.RELEASE

## 导航

* [基础](#webflux)
* [整合security](#security)
* [总结](#总结)

## webflux

响应式编程，只需少量的线程，即可实现大量的并发。

### 依赖

```xml
<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-webflux</artifactId>
 <version>${spring.boot.version}</version>
</dependency>

<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
 <version>${spring.boot.version}</version>
</dependency>
```

目前reactive暂不支持MySQL。

### 配置mongo连接

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://user:123456@127.0.0.1:27017/spring-boot-v2
```

### jpa方式编写dao层

我们可以通过`ReactiveMongoTemplate`模版类进行BaseDao的自定义，也可以通过继承`ReactiveMongoRepository`接口获得通用方法的能力。

我们可以提供更丰富的通用接口，首先继承`ReactiveMongoRepository`接口，然后添加新的接口方法即可。

```java
@NoRepositoryBean
public interface BaseReactiveMongoRepository<T extends Entity> extends ReactiveMongoRepository<T, String> {
  // 添加通用自定义方法
}
```

我们其他的dao继承自定义的通用接口即可

```java
@Repository
public interface DemoRepository extends BaseReactiveMongoRepository<Demo> {
}
```

### service层

对于一些通用的service方法，也可以采用通用的形式。

```java
public interface BaseReactiveService<T extends Entity> {

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return Mono<T>
     */
    Mono<T> findById(String id);

    /**
     * 根据ids查询
     *
     * @param ids 主键
     * @return Flux<T>
     */
    Flux<T> findByIds(List<String> ids);

    /**
     * 分页查询
     *
     * @param pageable 分页信息
     * @return Flux<T>
     */
    Flux<T> findAll(Pageable pageable);

    /**
     * 新增
     *
     * @param entity 实体
     * @return Mono<T>
     */
    Mono<T> add(T entity);

    /**
     * 修改，字段不为null则修改
     *
     * @param entity 实体
     * @return Mono<T>
     */
    Mono<T> modify(T entity);

    /**
     * 删除
     *
     * @param id 主键
     * @return Mono<Void>
     */
    Mono<Void> removeById(String id);

}
```

下面实现它

```java
public abstract class BaseReactiveServiceImpl<T extends Entity> implements BaseReactiveService<T> {

    @Autowired
    private BaseReactiveMongoRepository<T> reactiveMongoRepository;

    @Override
    public Mono<T> findById(String id) {
        return reactiveMongoRepository.findById(id);
    }

    @Override
    public Flux<T> findByIds(List<String> ids) {
        return reactiveMongoRepository.findAllById(ids);
    }

    @Override
    public Flux<T> findAll(Pageable pageable) {
        return reactiveMongoRepository.findAll(pageable);
    }

    @Override
    public Mono<T> add(T entity) {
        // 创建时间、更新时间
        Date now = new Date();
        if (entity.getCreateAt() == null) {
            entity.setCreateAt(now);
        }
        if (entity.getUpdateAt() == null) {
            entity.setUpdateAt(now);
        }
        return reactiveMongoRepository.insert(entity);
    }

    @Override
    public Mono<T> modify(T entity) {
        return reactiveMongoRepository.findById(entity.getId()).flatMap(persist -> doUpdate(persist, entity));
    }

    private Mono<T> doUpdate(T persist, T entity) {
        EntityUtils.copyEntityIgnoreNull(entity, persist);
        // 修改时间
        persist.setUpdateAt(new Date());
        return reactiveMongoRepository.save(persist);
    }

    @Override
    public Mono<Void> removeById(String id) {
        return reactiveMongoRepository.deleteById(id);
    }
}
```

这样就提供了最基本的crud方法，下面进行service编写

先编写接口

```java
public interface DemoService extends BaseReactiveService<Demo> {
}
```

然后实现接口并继承base service的实现类

```java
@Service
public class DemoServiceImpl extends BaseReactiveServiceImpl<Demo> implements DemoService {
}
```

### controller层

很多人不知道controller也是可以通用的

```java
public abstract class BaseReactiveController<T extends Entity> {

    @Autowired
    private BaseReactiveService<T> reactiveService;

    @GetMapping("/{id}")
    public Mono<T> show(@PathVariable String id) {
        return reactiveService.findById(id);
    }

    @PostMapping
    public Mono<T> add(@RequestBody T entity) {
        return reactiveService.add(entity);
    }

    @PutMapping
    public Mono<T> modify(@RequestBody T entity) {
        return reactiveService.modify(entity);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remove(@PathVariable String id) {
        return reactiveService.removeById(id);
    }

}
```

具体实现类继承即可

```java
@RestController
@RequestMapping("/api/demo")
public class DemoController extends BaseReactiveController<Demo> {
}
```

## security

webflux在使用security时，还是有些不一样的地方，下面介绍security的集成与使用。

### 依赖

同样是依赖`spring-boot-starter-security`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>${spring.boot.version}</version>
</dependency>
```

### 配置前

在正式配置前，我们简单的编写User实体类

```java
@Document(collection = "User")
public class User extends Entity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色
     */
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
```

mongo bson数据如下：

```json
// 1
{
    "_id": ObjectId("5bf254fbe5e5af13ec6d6140"),
    "username": "tony",
    "password": "$2a$10$OK9iNeJEvTi.Ouapyt98We8LosdtFXwvPZNldocMQ1sclQ4SVQmb2",
    "name": "托尼",
    "roles": [
        "ROLE_USER"
    ],
    "createAt": ISODate("2018-11-19T06:15:23.163Z"),
    "updateAt": ISODate("2018-11-19T11:52:43.274Z")
}

// 2
{
    "_id": ObjectId("5bf25503e5e5af13eef2689b"),
    "username": "admin",
    "password": "$2a$10$.3tniOJpTGkdETjoe.ydCOKwdn6Q3JUrv0h/GCbGaajnOXoyjOpxi",
    "name": "admin",
    "roles": [
        "ROLE_USER",
        "ROLE_ADMIN"
    ],
    "createAt": ISODate("2018-11-19T06:15:31.047Z"),
    "updateAt": ISODate("2018-11-19T06:15:31.047Z")
}
```

### 配置

添加两个注解：

* 注解`@EnableWebFluxSecurity`启用webflux相关的安全配置
* 注解`@EnableReactiveMethodSecurity`开启全局方法拦截

下面进行最基本的配置

```java
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                // 禁用csrf
                .csrf().disable()
                // login
                .formLogin();
        return http.build();
    }

    /**
     * 自定义ReactiveUserDetailsService
     *
     * @param userService UserService
     * @return ReactiveUserDetailsServiceImpl
     */
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserService userService) {
        return new ReactiveUserDetailsServiceImpl(userService);
    }

    /**
     * 配置密码
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
```

与web中配置`UserDetailsService`类似，webflux中需要提供`ReactiveUserDetailsService`

我们先提供一个UserDetails的实现

```java
public class UserDetailsImpl implements UserDetails {

    private User user;
    private List<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        Assert.notNull(user, "user not be null");
        this.user = user;
        this.authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

再自定义`ReactiveUserDetailsService`实现

```java
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    public ReactiveUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByUsername(username).map(UserDetailsImpl::new);
    }

}
```

### 全局方法拦截

在方法上使用`@PreAuthorize`注解进行相关的权限判断

```java
@RestController
@RequestMapping("/api/user")
// 需要用户权限
@PreAuthorize("hasRole('USER')")
public class UserController extends BaseReactiveController<User> {

    @Override
    @GetMapping("/{id}")
    // 需要管理员权限，优先级高于类上面的用户权限
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<User> show(@PathVariable String id) {
        return super.show(id).doOnNext(user -> user.setPassword("Unknown"));
    }

    @Override
    @PutMapping
    // 这里是需要管理员权限或者是本人修改（资产校验，防止被其他人修改信息）
    @PreAuthorize("hasRole('ADMIN') or #u.username == authentication.name")
    public Mono<User> modify(@P("u") @RequestBody User entity) {
        return super.modify(entity);
    }

    @Override
    @DeleteMapping("/{id}")
    // 需要管理员权限，优先级高于类上面的用户权限
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> remove(@PathVariable String id) {
        return super.remove(id);
    }
}
```

上面我们通过覆盖父类中的通用方法，让子类自定义自定义权限判断

## 总结

如果真的在业务中使用webflux，那肯定**步步皆坑**。

例如，我们在UserServiceImpl中覆盖父类的新增、修改方法，添加对密码的加密以及对角色的过滤（普通用户角色修改自己的信息时，不可以改角色）

```java
@Service
public class UserServiceImpl extends BaseReactiveServiceImpl<User> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<User> add(User user) {
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        return super.add(user);
    }

    @Override
    public Mono<User> modify(User user) {
        return Mono.just(user)
                // 加密
                .doOnNext(this::encodePassword)
                // 角色过滤
                .flatMap(ignore -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .doOnNext(authorities -> filterRoles(authorities, user))
                // 修改
                .flatMap(ignore -> super.modify(user));
    }

    /**
     * 加密密码
     *
     * @param user User
     */
    private void encodePassword(User user) {
        String password = user.getPassword();
        if (!StringUtils.isEmpty(password)) {
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }
    }

    /**
     * 管理员才能修改角色
     *
     * @param authorities Collection<? extends GrantedAuthority>
     * @param user User
     */
    private void filterRoles(Collection<? extends GrantedAuthority> authorities, User user) {
        boolean isAdmin = authorities.stream().anyMatch(authority -> SecurityConstant.ROLE_ADMIN.equals(authority.getAuthority()));
        if (!isAdmin) {
            user.setRoles(null);
        }
    }
}
```

注意modify方法中

```java
Mono.just(user)
    // 加密
    .doOnNext(this::encodePassword)
    // 角色过滤
    .flatMap(ignore -> ReactiveSecurityContextHolder.getContext())
    .map(SecurityContext::getAuthentication)
    .map(Authentication::getAuthorities)
    .doOnNext(authorities -> filterRoles(authorities, user))
    // 修改
    .flatMap(ignore -> super.modify(user));
```

这样的代码，也许你感觉还行，但是业务更复杂些呢？