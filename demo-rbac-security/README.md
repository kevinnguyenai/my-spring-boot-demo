# spring-boot-demo-rbac-security

> This DEMO mainly demonstrates how the Spring Boot project integrates Spring Security to complete the permission interception operation. This DEMO is the back -end authority management part based on the front and rear end of **, which is different from the template technology used in other blogs. I hope it will be helpful to everyone.

## 1. Main features

- [X] Based on the design of the `rbac` permissions model, details refer to the database table structure design [` security.sql`] (./ SQL/Security.sql)
- [X] Support ** dynamic permissions management **, please refer to [`` rbacAuthorityService.java`] (./ SRC/Main/Java/COM/XKCODING/RBAC/Security/Config/RBacAutHorvice.java)
- [X] ** Login/ Login ** Partially uses custom controller implementation, without using the default implementation of the `Spring Security`, suitable for the front and back -end separation items. For details, please refer to [` SecurityConfig.java`] (././ src/main/java/com/xkcoding/rbac/security/config/SecurityConfig.java) 和[`AuthController.java`](./src/main/java/com/xkcoding/rbac/security/controller/AuthController.java Cure
-[X] Prespective technology uses `Spring-Data-JPA` to complete
- [X] Use `jwt` to implement security verification, and introduce` redis` to solve the disadvantages of expiration of expiration, and ensure that the same user only supports the same device login at the same time, and the login will be available in different equipment. For details, please refer to [ `Jwtutil.java`] (./ src/main/java/com/xkcoding/rbac/security/util/jwtutil.java)
- [X] Online number statistics, details Reference [`Monitorservice.java`] (./ SRC/Main/Java/COM/XKCODING/RBAC/Security/Service/Monitorservice.java) and [` Redisutil.java`]]] /src/main/java/com/xkcoding/Rbac/Security/Util/redisutil.java)
- [X] Manually kick out the user, details refer to [`Monitorservice.java`] (./ SRC/Main/Java/COM/XKCODING/RBAC/Security/Service/Monitorservice.java)
- [X] Custom configuration does not require interception requests, details refer to [`Customconfig.java`] (./ SRC/Main/Java/COM/XKCODING/RBAC/Config/CustomConfig.java) and [` Application .yml`] (./ src/main/resources/application.yml)

## 2. Run 

 ### 2.1. Environment 
 1. JDK 1.8 or higher 
  2. Maven 3.5 or higher 
  3. MySQL 5.7 or higher 
  4. Redis

### 2.2. 

 1. Create a database called `spring-boot-design. The character set is set to` UTF-8`. .url` 
 2. Use [`security.sql`] (./ SQL/Security.sql) This SQL file, create a database table and initialize RBAC data 
 3. Run `SpringBootDemorbacSecurityApplication` 
 4. Administrator account number: admin/123456 Ordinary user: User/123456 
 5. Use the `post` request to access`/$ {contextpath}/api/auth/login` endpoint, enter the account password, return to the token after logging in, place the obtained token in the specific request. `, Value prefix is` Bearer, add a space behind `plus token, and add the parameters of specific requests, just 
 6. enjoy ~: kissing_smiling_eyes:

## 3. 部分关键代码

### 3.1. pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-demo-rbac-security</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>spring-boot-demo-rbac-security</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.xkcoding</groupId>
        <artifactId>spring-boot-demo</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <jjwt.veersion>0.9.1</jjwt.veersion>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

       <!-Object pool, you must introduce when using redis->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>${jjwt.veersion}</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>spring-boot-demo-rbac-security</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

### 3.2. JwtUtil.java

> JWT tool class, the main function: generate JWT coexistence in redis, analyze JWT and verify its accuracy, get JWT from the Header of Request

```java

@EnableConfigurationProperties(JwtConfig.class)
@Configuration
@Slf4j
public class JwtUtil {
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

     /** 
      * Create JWT 
      * 
      * @param rememberme remember me 
      * @param ID user ID 
      * @param Subject username 
      * @param ROLES user role 
      * @param Authorities user permission 
      * @Return jwt 
      */
    public String createJWT(Boolean rememberMe, Long id, String subject, List<String> roles, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getKey())
                .claim("roles", roles)
                .claim("authorities", authorities);
        // Set the expiration time 
         Long ttl = rememberme? Jwtconfig.getremember (): jwtconfig.getttl (); 
         if (ttl> 0) {{ 
             Builder.Setexpiration (dateutil.offsetmilliseCond (now, ttl.intvalue ())); 
         } 

         String jwt = builder.compact (); 
         // Save the generated JWT to Redis 
         StringRedistemPlate.Opsforvalue () 
                 .sets.redis_jwt_key_prefix + Subject, JWT, TTL, Timeunit.milliseConds); 
         Return jwt;
    }

     /** 
      * Create JWT 
      * 
      * @param authentication user certification information 
      * @param rememberme remember me 
      * @Return jwt 
      */
    public String createJWT(Authentication authentication, Boolean rememberMe) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJWT(rememberMe, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRoles(), userPrincipal.getAuthorities());
    }

     /** 
      *analyzeJWT 
      * 
      * @param JWT JWT 
      * @return {@link class} 
      *//
    public Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getKey())
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            String redisKey = Consts.REDIS_JWT_KEY_PREFIX + username;

            // Whether the JWT in Redis exists
            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new SecurityException(Status.TOKEN_EXPIRED);
            }

            // Whether the JWT in the Redis is consistent with the current, inconsistency means that the user has canceled it/users log in at different devices. They all represent that the JWT has expired
            String redisToken = stringRedisTemplate.opsForValue()
                    .get(redisKey);
            if (!StrUtil.equals(jwt, redisToken)) {
                throw new SecurityException(Status.TOKEN_OUT_OF_CTRL);
            }
            return claims;
        } catch (ExpiredJwtException e) {
            log.error("Token expired");
            throw new SecurityException(Status.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Token");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
            log.error("Token invalid");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        } catch (SignatureException e) {
            log.error("Effective token signature");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Token Parameters do not exist");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        }
    }

    /**
      * Set jwt expiration 
      * 
      * @param Request request
     */
    public void invalidateJWT(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        String username = getUsernameFromJWT(jwt);
        // Clear JWT from Redis
        stringRedisTemplate.delete(Consts.REDIS_JWT_KEY_PREFIX + username);
    }

    /** 
      * Get the username according to JWT 
      * 
      * @param jwt jwt 
      * @Return Username 
      */
    public String getUsernameFromJWT(String jwt) {
        Claims claims = parseJWT(jwt);
        return claims.getSubject();
    }

    /** 
      * Get jwt from the Header of Request 
      * 
      * @param Request request 
      * @Return jwt 
      */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
```

### 3.3. SecurityConfig.java

> Spring Security 配置类，主要功能：配置哪些URL不需要认证，哪些需要认证

```java

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CustomConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()

                // Close CSRF
                .and()
                .csrf()
                .disable()

                // Login behavior, refer to AuthController#login
                .formLogin()
                .disable()
                .httpBasic()
                .disable()

                // Certification request
                .authorizeRequests()
                // 所有请求都需要登录访问
                .anyRequest()
                .authenticated()
                // RBAC dynamic URL certification
                .anyRequest()
                .access("@rbacAuthorityService.hasPermission(request,authentication)")

                // Realize by yourself, refer to AuthController#logout
                .and()
                .logout()
                .disable()

                // Session management
                .sessionManagement()
                // Because you have used jwt, you do not manage the session here
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // Abnormal treatment
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

        // Add custom JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Play all requests that can be accessed without logging in, see AuthController
     * You can also configure in {@Link #Configure (httpsecurity)}
     * {@code http.authorizeRequests().antMatchers("/api/auth/**").permitAll()}
     */
    @Override
    public void configure(WebSecurity web) {
        WebSecurity and = web.ignoring()
                .and();

        // Ignore get
        customConfig.getIgnores()
                .getGet()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.GET, url));

        // Ignore post
        customConfig.getIgnores()
                .getPost()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.POST, url));

        // Ignore delete
        customConfig.getIgnores()
                .getDelete()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.DELETE, url));

        // Ignore put
        customConfig.getIgnores()
                .getPut()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.PUT, url));

        // Ign
        customConfig.getIgnores()
                .getHead()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.HEAD, url));

        // Ignore patch
        customConfig.getIgnores()
                .getPatch()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.PATCH, url));

        // Ignore Options
        customConfig.getIgnores()
                .getOptions()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.OPTIONS, url));

        // Ignore trace trace
        customConfig.getIgnores()
                .getTrace()
                .forEach(url -> and.ignoring()
                        .antMatchers(HttpMethod.TRACE, url));

        // Councing according to the request format
        customConfig.getIgnores()
                .getPattern()
                .forEach(url -> and.ignoring()
                        .antMatchers(url));

    }
}
```

### 3.4. RbacAuthorityService.java

> Routing dynamic authentication category, the main function: 
 Forecast 
 > 1. Check the legitimacy of requests, exclude the two abnormal requests of 404 and 405 
 > 2. Matching the resource that the user can access according to the current request path, you can access it through the pass, otherwise, you will not be allowed to access

```java

@Component
public class RbacAuthorityService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RequestMappingHandlerMapping mapping;

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        checkRequest(request);

        Object userInfo = authentication.getPrincipal();
        boolean hasPermission = false;

        if (userInfo instanceof UserDetails) {
            UserPrincipal principal = (UserPrincipal) userInfo;
            Long userId = principal.getId();

            List<Role> roles = roleDao.selectByUserId(userId);
            List<Long> roleIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());
            List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);

            //Obtain resources, separate the front and rear end, so the filtering page is right, only the button permissions are retained
            List<Permission> btnPerms = permissions.stream()
                    // Filter page Facial permanent
                    .filter(permission -> Objects.equals(permission.getType(), Consts.BUTTON))
                    // Filter URL to empty
                    .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                    //Filtering Method is empty
                    .filter(permission -> StrUtil.isNotBlank(permission.getMethod()))
                    .collect(Collectors.toList());

            for (Permission btnPerm : btnPerms) {
                AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(btnPerm.getUrl(), btnPerm.getMethod());
                if (antPathMatcher.matches(request)) {
                    hasPermission = true;
                    break;
                }
            }

            return hasPermission;
        } else {
            return false;
        }
    }

      /** 
      * Whether the request exists 
      * 
      * @param Request request 
      */
    private void checkRequest(HttpServletRequest request) {
        // The method of getting the current request
        String currentMethod = request.getMethod();
        Multimap<String, String> urlMapping = allUrlMapping();

        for (String uri : urlMapping.keySet()) {
             // Match URL through AntpathRequestMatcher 
             // You can create AntpathRequestMatcher in two ways 
             // 1: New AntpathRequestMatcher (URI, Method) This method can directly determine whether the method is matched, because we do not match the method and throw it out, so we use the second way to create the second way to create 
             // 2: New AntpathRequestMatcher (URI) This method does not check the request method, only checks the request path
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(uri);
            if (antPathMatcher.matches(request)) {
                if (!urlMapping.get(uri)
                        .contains(currentMethod)) {
                    throw new SecurityException(Status.HTTP_BAD_METHOD);
                } else {
                    return;
                }
            }
        }

        throw new SecurityException(Status.REQUEST_NOT_FOUND);
} 

     /** 
      * Get all URL MAPPING, return the format to {"/test": ["get", "post"], "/sys": ["get", "delete"]} 
      * 
      * @Return {@Link ArrayListMultimap} The URL MAPPING in the format
     */
    private Multimap<String, String> allUrlMapping() {
        Multimap<String, String> urlMapping = ArrayListMultimap.create();
        // Get the corresponding information of the URL and the class and methods
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((k, v) -> {
            // 获取当前 key 下的获取所有URL
            SeGet all the URLs under the current keyetPatternsCondition()
                    .getPatterns();
            RequestMethodsRequestCondition method = k.getMethodsCondition();

           // Add all the request methods to each url
            url.forEach(s -> urlMapping.putAll(s, method.getMethods()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.toList())));
        });

        return urlMapping;
    }
}
```

### 3.5. JwtAuthenticationFilter.java

> JWT certification filter, the main function: 
 Forecast 
 > 1. Filter the request that does not need to be intercepted 
 > 2. According to the current request JWT, authentication user identity information

```java

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomConfig customConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIgnores(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.getJwtFromRequest(request);

        if (StrUtil.isNotBlank(jwt)) {
            try {
                String username = jwtUtil.getUsernameFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (SecurityException e) {
                ResponseUtil.renderJson(response, e);
            }
        } else {
            ResponseUtil.renderJson(response, Status.UNAUTHORIZED, null);
        }

    }

    /**
* Whether the request does not require permission interception 
      * 
      * @param Request Current Request 
      * @Return true -ignore, false -not ignored
     */
    private boolean checkIgnores(HttpServletRequest request) {
        String method = request.getMethod();

        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (ObjectUtil.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }

        Set<String> ignores = Sets.newHashSet();

        switch (httpMethod) {
            case GET:
                ignores.addAll(customConfig.getIgnores()
                        .getGet());
                break;
            case PUT:
                ignores.addAll(customConfig.getIgnores()
                        .getPut());
                break;
            case HEAD:
                ignores.addAll(customConfig.getIgnores()
                        .getHead());
                break;
            case POST:
                ignores.addAll(customConfig.getIgnores()
                        .getPost());
                break;
            case PATCH:
                ignores.addAll(customConfig.getIgnores()
                        .getPatch());
                break;
            case TRACE:
                ignores.addAll(customConfig.getIgnores()
                        .getTrace());
                break;
            case DELETE:
                ignores.addAll(customConfig.getIgnores()
                        .getDelete());
                break;
            case OPTIONS:
                ignores.addAll(customConfig.getIgnores()
                        .getOptions());
                break;
            default:
                break;
        }

        ignores.addAll(customConfig.getIgnores()
                .getPattern());

        if (CollUtil.isNotEmpty(ignores)) {
            for (String ignore : ignores) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }

        return false;
    }

}
```

### 3.6. CustomUserDetailsService.java

> Implement the `Userdetailsservice` interface, the main function: query user information according to the user name

```java

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrPhone) throws UsernameNotFoundException {
        User user = userDao.findByUsernameOrEmailOrPhone(usernameOrEmailOrPhone, usernameOrEmailOrPhone, usernameOrEmailOrPhone)
                .orElseThrow(() -> new UsernameNotFoundException("User information is not found: " + usernameOrEmailOrPhone));
        List<Role> roles = roleDao.selectByUserId(user.getId());
        List<Long> roleIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());
        List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);
        return UserPrincipal.create(user, roles, permissions);
    }
}
```

### 3.7. RedisUtil.java

> Main functions: Get the key list existence of Redis according to the page of the key format

```java

@Component
@Slf4j
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
* Pagling to obtain the specified format Key, use the scan command instead of the keys command, which can improve the query efficiency under the situation of large data volume 
      * 
      * @param Patternky Key format 
      * @param Currentpage Current Page Number 
      * @param PageSize number per page 
      * @Return Pagling Get the specified format key
     */
    public PageResult<String> findKeysForPage(String patternKey, int currentPage, int pageSize) {
        ScanOptions options = ScanOptions.scanOptions()
                .match(patternKey)
                .build();
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection rc = factory.getConnection();
        Cursor<byte[]> cursor = rc.scan(options);

        List<String> result = Lists.newArrayList();

        long tmpIndex = 0;
        int startIndex = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            if (tmpIndex >= startIndex && tmpIndex < end) {
                result.add(key);
            }
            tmpIndex++;
        }

        try {
            cursor.close();
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception e) {
            log.warn("RedisConnect to close abnormality，", e);
        }

        return new PageResult<>(result, tmpIndex);
    }
}
```

### 3.8. MonitorService.java

> Monitoring service, the main function: query the pagination list of the current online number, manually kick out a user

```java
package com.xkcoding.rbac.security.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.xkcoding.rbac.security.common.Consts;
import com.xkcoding.rbac.security.common.PageResult;
import com.xkcoding.rbac.security.model.User;
import com.xkcoding.rbac.security.repository.UserDao;
import com.xkcoding.rbac.security.util.RedisUtil;
import com.xkcoding.rbac.security.vo.OnlineUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MonitorService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDao userDao;

    public PageResult<OnlineUser> onlineUser(Integer page, Integer size) {
        PageResult<String> keys = redisUtil.findKeysForPage(Consts.REDIS_JWT_KEY_PREFIX + Consts.SYMBOL_STAR, page, size);
        List<String> rows = keys.getRows();
        Long total = keys.getTotal();

 // Obtain the user list according to the key in Redis
        List<String> usernameList = rows.stream()
                .map(s -> StrUtil.subAfter(s, Consts.REDIS_JWT_KEY_PREFIX, true))
                .collect(Collectors.toList());
       // Query user information according to the username
        List<User> userList = userDao.findByUsernameIn(usernameList);

       // Encourage online user information
        List<OnlineUser> onlineUserList = Lists.newArrayList();
        userList.forEach(user -> onlineUserList.add(OnlineUser.create(user)));

        return new PageResult<>(onlineUserList, total);
    }
}
```

### 3.9. See the rest of the code 

 ## 4. Reference 

 1. Spring Security official document: https: //docs.spring.io/spring-spring-site/docs/5.1.1.release/htmlsingle/ 
 2. JWT official website: https://jwt.io/ 
 3. JJWT open source tool Reference: https://github.com/jwtk/jjwt#quickstart 
 4. The authorized part of the authorized part of the official document: https://docs.spring.io/spring-spring-site/docs/5.1.1.Release/htmlsingle/#Autility 

 4. Dynamic authorization section, refer to blog: https://blog.csdn.net/larger5/article/details/81063438
