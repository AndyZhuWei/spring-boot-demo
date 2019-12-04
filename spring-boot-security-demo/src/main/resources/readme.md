## 9.1安全控制Spring Security
### 9.1.1 Spring Security快速入门
1.什么是Spring Security
Spring Security是专门针对基于Spring的项目的安全框架，充分利用了依赖注入和AOP来实现安全的功能。
在早期的Spring Security版本，使用Spring Security需要使用大量的XML配置，而本节将全部基于Java配置来实现Spring Security的功能。
安全框架有两个重要的概念,即认证(Authenticatin)和授权(Authorization)。认证即确认用户可以访问当前系统；授权即确定用户在当前系统下
所拥有的功能权限，本节将围绕认证和授权展开。
2.Spring Security的配置
（1）Spring Security为我们提供了一个多个过滤器来实现所有安全的功能，我们只需注册一个特殊的DelegatingFilterProxy过滤器
到WebApplicationInitializer即可
而在实际使用中，我们只需让自己的Initializer类继承AbstractSecurityWebApplicationInitializer抽象类即可。
AbstractSecurityWebApplicationInitializer实现了WebApplicationInitializer接口，并通过onStartup方法调用：
insertSpringSecurityFilterChain(servletContext);
它为我们注册了DelegatingFilterProxy。insertSpringSecurityFilterChain源码如下：
```java
private void insertSpringSecurityFilterChain(ServletContext servletContext) {
    String filterName = DEFAULT_FILTER_NAME;
    DelegatingFilterProxy springSecurityFilterChain = new 
           DelegatingFilterProxy(filterName);
    String contextAttribute = getWebApplicationContextAttribute();
    if(contextAttribute != null) {
        springSecurityFilterChain.setContextAttribute(contextAttribute);
    }
    registerFilter(servletContext,true,filterName,springSecurityFilterChain);
}
```
所以我们只需用以下代码即可开启Spring Security的过滤器支持：
```java
public class AppInitializer extends AbstractSecurityWebApplicationInitializer {
    
}
```
(2)配置
Spring Security的配置和Spring MVC的配置类似，只需在一个配置类上注解@EnableWebSecurity,并让这个类继承WebSecurityConfigurerAdapter
即可。我们可以通过重写configure方法来配置相关的安全配置。
```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configre(auth);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
```
3.用户认证
认证需要我们有一套用户数据的来源，而授权则是对于某个用户有相应的角色权限。在Spring Security里我们通过重写
protected void configure(AuthenticationManagerBuilder auth)
方法来实现定制。
（1）内存中的用户
使用AuthenticationManagerBuilder的inMemoryAuthenication方法即可添加在内存中的用户，并可给用户指定角色权限
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
         .withUser("wyf").password("wyf").roles("ROLE_ADMIND")
         .and()
         .withUser("wisely").password("wisely").roles("ROLE_USER");
}
```
(2)JDBC中的用户
JDBC中的用户直接指定dataSource即可。
```java
@Autowired
DataSource dataSource;

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource);
}
```
不过这看上去很奇怪，其实这里的Spring Security是默认了额你的数据库结构的。通过jdbcAuthentication的源码，我们可以看出在JdbcDaoImpl中
定义了默认的用户及角色权限获取的SQL语句：
```java
public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,enabled from users where username = ?";
public static final String DEF_AUTHORIZIES_BY_USERNAME_QUERY = "select username,authority from authorities where username = ?";
```
当然我们可以自定义我们的查询用户和权限的SQL语句，例如：
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource)
       .usersByUsernameQuery("select username,password,true from myusers where username = ?")
       .authoritiesByUsernameQuery("select username,role from roles where username = ?");
}
```
(3)通用的用户
上面的两种用户和权限的获取方式只限于内存或者JDBC，我们的数据访问方式可以是多种各样的，可以是非关系型数据库，也可以是我们常用的JPA等
这时我们需要自定义实现UserDetailService接口。上面的内存中用户及JDBC用户就是UserDetailsService的实现。定义如下：
```java
public class CustomUserService implements UserDetailsService {
    @Autowired
    SysUserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(username);
        List<GrantedAuthroity> authroities = new ArrayList<GrantedAuthroity>();
        authroities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(user.getUsername(),user.getPassword(),authroities);
    }
}
```
除此之外，我们还需要注册这个CustomUserService，代码如下：
```java
@Bean
UserDetailsService customUserService() {
    return new CustomUserService();
}

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailService(customUserService());
}
```
4.请求授权
Spring Security是通过重写
protected void configure(HttpSecurity http)
方法来实现请求拦截的。
Spring Security使用以下匹配器匹配请求路径:
**antMatchers:** 使用Ant风格的路径匹配
**regexMatchers:** 使用正则表达式匹配路径
anyRequest:匹配所有请求路径
在匹配了请求路径后，需要针对当前用户的信息对请求路径进行安全处理，Spring Security提供了如下的安全处理方法。
方法                     用途
access(String)          Spring EL表达式结果为true时可访问
anonymous()             匿名可访问
denyAll()               用户不能访问
fullyAuthenticated()    用户完全认证可访问(非remember me下自动登录)
hasAnyAuthority(String...) 如果用户有参数，则其中任一权限可访问
hasAnyRole(String...)   如果用户有参数，则其中任一角色可访问
hasAuthority(String)    如果用户有参数，则其权限可访问
hasIpAddress(String)    如果用户来自参数中的IP则可访问
hasRole(String)         若用户有参数中的角色可访问
permitAll()             用户可任意访问
rememberMe()            允许通过remember-me登录的用户访问
authenticated()         用户登录后可访问
我们可以看一下下面的示例代码：
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
    .authorizeRequests()//1通过authorizeRequest方法来开始请求权限配置
    .antMatchers("/admin/**").hasRole("ROLE_ADMIN")//2请求匹配/admin/**，只有拥有ROLE_ADMIN角色的用户可以访问
    .antMathcers("/user/**").hasAnyRole("ROLE_ADMIN","ROLE_USER")//3请求匹配/user/**,拥有ROLE_ADMIN或ROLE_USER角色的用户都可以访问
    .anyRequest().authenticated();//4其余所有的请求都需要认证后(登录后)才可访问
}
```
5.定制登录行为
我们也可以通过重写
protected void configure(HttpSecurity http)方法来定制我们的登录行为
下面将重用的登录行为的定制以简单的代码演示：
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
       .formLogin()//1通过formLogin方法定制登录操作
       .loginPage("/login")//2使用loginPage方法定制登陆页面的访问地址
       .defaultSuccessUrl("/index")//3defaultSuccessUrl指定登录成功后转向的页面
       .failureUrl("/login?error")//4failureUrl指定登录成功后转向的页面
       .permitAll()
       .and()
       .rememberMe()//5rememberMe开启cookie存储用户信息
           .tokenValiditySeconds(1209600)//6tokenValiditySeconds指定cookie有效期为1209600，即2个星期
           .key("myKey")//7key指定cookie中的私钥
       .and()
       .logout()//8使用logout方法定制注销行为
           .logoutUrl("/custom-logout")//9logoutUrl指定注销的URL路径
           .logoutSuccessUrl("/logout-success")//10logoutSuccessUrl指定注销成功后转向的页面
           .permitAll();
}
```
###9.1.2 Spring Bootd的支持
 Spring Boot针对Spring Security的自动配置在org.springframework.boot.autoconfigure.security包中。
 主要通过SecurityAutoConfiguraton和SecurityProperties来完成配置
 SecurityAutoConfiguration导入了SpringBootWebSecurityConfiuration中的配置。在SpringBootWebSecurityConfiguration配置中，我们
 获得如下的自动配置：
 1）自动配置了一个内存中的用户，账号为user,密码在程序启动时出现。
 2）忽略/css/**、/js/**、/images/**和/**/favicon.ico等静态文件的拦截
 3）自动配置的securityFilterChainRegistration的Bean
 SecurityProperties使用以"security"为前缀的属性配置Spring Security相关的配置，包含：
 security.user.name=user#内存中的用户默认账号为user
 security.user.password=#1默认用户的密码
 security.user.role=USER#默认用户的角色
 security.require-ssl=false#是否需要ssl支持
 security.enable-csrf=false#是否开启“跨站请求伪造”支持，默认关闭
 security.basic.enabled=true
 security.basic.realm=Spring
 security.basic.path=#/**
 security.basic.authorized-mode=
 security.filter-order=0
 security.headers.xss=false
 security.headers.cache=false
 security.headers.frame=false
 security.headers.content-type=false
 security.headers.hsts=all
 security.sessions=stateless
 security.ignored=#用逗号隔开的无须拦截的路径
 Spring Boot为我们做了如此多的配置，当我们需要自己扩展配置的时，只需配置类继承WebSecurityConfigurerAdapter类即可，无须使用
 @EnableWebSecurity注解
 ```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
}
```
### 9.1.3实战
在本节的示例中，我们将演示使用Spring Boot下的Spring Security的配置，完成简单认证授权的功能。此节我们将通过Spring Data JPA获得用户数据。
页面模板使用Thymeleaf,Thymeleaf也为我们提供了支持Spring Security的标签
1.新建Spring Boot项目
spirng-boot-security-demo
新建Spring Boot项目，依赖为JPA、Security(Spring-boot-starter-security)、Thymeleaf(spring-boot-starter-thymeleaf)
添加Oracle驱动及Thymeleaf的Spring Security的支持
配置applicaiton.properties以及把bootstrap.min.css放置在src/main/resources/static/css下，此路径默认不拦截
2.用户和角色
我们使用JPA来定义用户和角色
用户：
SysUser
角色：
SysRole
（1）数据结构及初始化
当我们配置用户和角色的多对多关系后，通过设置
spring.jpa.hibernate.ddl-auto=update
为我们自动生成用户表:SYS_USER、角色表:SYS_ROLE、关联表SYS_USER_ROLES
针对上面的表结构，我们初始化一些数据来方便我们演示。在src/main/resources下，新建data.sql,
即新建两个用户，角色分别为ROLE_ADMIN和ROLE_USER，代码如下：
（2）传值对象
用来测试不同角色用户的数据展示:
Msg
3.数据访问
我们这里的数据访问很简单，代码如下：
SysUserRepository
4.自定义UserDetailsService
CustomUserService
5.配置
（1）Spring MVC配置：
WebMvcConfig
（2）Spring Security配置：
WebSecurityConfig
6.页面
（1）登录页面
login.html
（2）首页
home.html
7.控制器
此控制器很简单，只为首页显示准备数据:
HomeController
8.运行
(1)登录。访问http://localhost:8080,将会自动转到登录页面http://localhost:8080/login
使用正确的账号密码登录
使用错误的账号密码登录
(2)注销。登录成功后，单击注销按钮
(3)用户信息
页面上我们将用户名显示在页面的标题上
(4)视图控制
wyf和wisely用户角色不同，因此获得不同的视图
注意：测试时发现页面标签sec:authentication不起作用，最后在pom.xml文件中加入一下属性即可
  <properties>
        <!-- upgrade to thymeleaf version 3 -->
        <thymeleaf.version>3.0.8.RELEASE</thymeleaf.version>
        <thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
        <thymeleaf-extras-springsecurity4.version>3.0.2.RELEASE</thymeleaf-extras-springsecurity4.version>
    </properties>
还没有测试通过























































