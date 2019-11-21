# 第5章 Spring Boot基础
# 5.1.1 什么是Spring Boot
随着动态语言的流行（Ruby\Groovy\Scala\Node.js），Java的开发显得格外的笨重；繁多的配置、地下的开发效率、复杂的部署流程以及第三方技术集成难度大
在上述环境下，Spring Boot应用而生。它使用“习惯优于配置”的理念让你的项目快速运行起来。使用Spring Boot很容易创建一个独立运行（运行jar,
内嵌Servlet容器）\准生产级别的基于Spring框架的项目，使用Spring Boot你可以不用或者只需要很少的Spring配置。

### 5.1.2 Spring Boot核心功能
1.独立运行的Spring项目
2.内嵌Servlet容器
3.提供starter简化maven配置
4.自动配置Spring
5.准生产的应用监控
6.无代码生成和xml配置
Spring Boot的神奇不是借助于代码生成来实现的，而是通过条件注解来实现的，这是Spring4.x提供的新特性
Spring4.x提倡使用Java配置和注解配置组合，而Sprig Boot不需要任何xml配置即可实现Spring的所有配置

### 5.1.3 Spring Boot的优缺点
**优点**
1.快速构建项目
2.对主流开发框架的无配置集成
3.项目可独立运行，无须外部依赖Servlet容器
4.提供运行时的应用监控
5.极大地提高了开发、部署效率
6.与云计算的天然集成。
**缺点**
1.文档较少且不够深入
2.如果不认同Spring框架，这也许是它的缺点

### 5.2.5 Maven手工构建
在pom.xml中添加Spring Boot的父级依赖，这样当前的项目就是Spring Boot项目了
spring-boot-starter-parent是一个特殊的starter,它用来提供相关的Maven默认医疗，使用它之后，常用的包依赖可以省去version
标签

# 第6章 Spring Boot核心
## 6.1基本配置
### 6.1.1 入口类和@SpringBootApplicatin
Spring Boot通常有一个名为*Application的入口类，入口类里有一个main方法，这个main方法其实就是一个标准的Java应用的入口方法。
在main方法中使用SpringApplication.run(Application.class, args)启动Spring Boot应用项目

@SpringBootApplication是Spring Boot的核心注解，它是一个组合注解，主要组合了@Configuration、@EnableAutoConfiguration
和@ComponentScan
其中，@EnableAutoConfiguration让Spring Boot根据类路径中的jar包依赖为当前项目进行自动配置，例如，添加了spring-boot-starter-web
依赖，会自动添加tomcat和Spring MVC的依赖，那么Spring Boot会对Tomcat和Spring MVC进行自动配置。
又如，添加了spring-boot-starter-data-jpa依赖，Spring Boot会自动进行JPA相关的配置
Spring Boot会自动扫描@SpringBootApplication所在类的同级包以及下级包里的Bean(若为JPA项目还可以扫描标注@Entity的实体类)
### 6.1.2 关闭特定的自动配置
关闭特定的自动配置应该使用@SpringBootApplication注解的exclude参数，例如：
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
### 6.1.3 定制Banner
1.修改Banner
在Sring Boot启动的时候会有一个默认启动图案，我们可以在src/main/resources下新建一个banner.txt，通过http://patorjk.com/software/tagg
网站生成字符串，将内容复制到banner.txt中即可。这时启动后就变为我们自己生成的图案了
2.关闭banner
在main里修改的内容如下：
```java
public static void main(String[] args) {
		//SpringApplication.run(Application.class, args);
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
```
或使用fluent API修改为：
new SpringApplicationBuilder(Application.class).bannerMode(Banner.Mode.OFF).run(args);
### 6.1.4 Spring Boot的配置文件
Spring Boot使用一个全局的配置文件application.properties或application.yml,放置在src/main/resources目录或者类路径的/config下
Spring Boot不仅支持常规的properties配置文件，还支持yaml语言的配置文件。yaml是以数据为中心的语言，在配置数据的时候具有面向对象的特性
Spring Boot的全局配置文件的作用是对一些默认配置的配置值进行修改
1.简单示例
将Tomcat的默认端口号8080修改为9090，并将默认的访问路径“/”修改为“/helloboot”
可以在applicaiton.properties中添加：
server.port=9090
server.context-path=/helloboot
或在application.yml中添加：
server:
  port: 9090
  contextPath: /helloboot
从上面的配置可以看出，在Spring Boot中，context-path\contextPath或者CONTEXT_PATH形式其实是通用的。并且，yaml的配置更简洁清晰
`待验证 @PropertySource注解是否支持yaml文件加载？`
### 6.1.6 使用xml配置
特殊情况下可以通过@ImportResource来加载xml配置
@ImportResource({"classpath:some-context.xml","classpath:another-context.xml"})

## 6.2外部配置
Spring Boot允许使用properties文件、yaml文件或者命令行参数作为外部配置。
### 6.2.1 命令行参数配置
Spring Boot可以是基于jar包运行的，打成jar包的程序可以直接通过下面命令运行：
java -jar xx.jar
可以通过以下命令修改Tomcat端口号
java -jar xx.jar --server.port=9090
### 6.2.2 常规属性配置
在常规Spring环境下，注入properties文件里的值的方式，通过@PropertySource指明properties文件的位置，然后通过@Value注入值。
在Spring Boot里，我们只需要在application.properties定义属性，直接使用@Value注入即可
### 6.2.3 类型安全的配置（基于properties）
使用@Value注入每个配置在实际项目中会显得格外麻烦，因为我们的配置通常会是许多个，若使用上例的方式则要使用@Value注入很多次
Spring Boot还提供了基于类型安全的配置方式，通过@ConfigurationProperties将properties属性和一个Bean以及属性关联，从而实现类型安全的配置
## 6.3 日志配置
Spring Boot支持Java Util Logging、Log4J、Log4J2和Logback作为日志框架，无论使用哪种日志框架，Spring Boot已为当前使用日志框架的控制台
输出及文件输出做好了配置
默认情况下，Spring Boot使用Logback作为日志框架
配置日志文件
logging.file=D:/mylog/log.log
配置日志级别：，格式为logging.level.包名=级别
logging.level.org.springframework.web=DEBUG
## 6.4 Profile配置
Profile是Spring用来针对不同的环境对不同的配置提供支持的，全局Profile配置使用application-{profile}.properties(如application-prod.properties)
通过在application.properties中设置spring.profiles.active=prod来指定活动的Profile

## 6.5 Spring Boot运行原理
Spring Boot关于自动配置的源码在spring-boot-autoconfigure-x.x.x.jar内，主要包含了如下配置
`autoconfigure.png`
若想知道Spring Boot为我们做了哪些自动配置，可以查看这里的源码
可以通过以下三种方式查看当前项目中已启用和未启用的自动配置的报告。
1.运行jar时增加--debug参数
java -jar xx.jar --debug
2.在application.properties中设置属性
debug=true
3.或者在VM arguments中输入-Ddebug
此时启动，可在控制台输出，
已启用的自动配置、未启用的自动配置
### 6.5.1 运作原理
关于Spirng Boot的运作原理，我们还是回归到@SpringBootApplication注解上来，这个注解是一个组合注解，
它的核心功能是由@EnableAutoConfiguration注解提供的。
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
```
这里的关键功能是@Import注解导入的配置功能，AutoConfigurationImportSelector使用SpringFactoriesLoader.loadFactoryNames方法来扫描具有
META-INF/spring.factories文件的jar包，此文件中声明了有哪些自动配置
### 6.5.2 核心注解
打开上面任意一个AutoConfiguration文件，一般都有下面的条件注解，

@ConditionalOnBean:当容器里有指定的Bean的条件下
@ConditionalOnClass:当类路径下有指定的类的条件下
@ConditionalOnExpression:基于SpEL表达式作为判断条件
@ConditionalOnJava:基于JVM版本作为判断条件
@COnditionalOnJndi:在JNDI存在的条件下查找指定的位置
@ConditionalOnMissingBean:当容器里没有指定Bean的情况下
@ConditionalOnMissingClass:当类路径下没有指定的类的条件下
@ConditionalOnNotWebApplicatin:当前项目不是Web项目的条件下
@ConditionalOnProperty:指定的属性是否有指定的值
@ConditionalOnResource:类路径是否有指定的值。
@ConditionalOnSingleCandidate:当指定Bean在容器中只有一个，或者虽然有多个但是指定首选的Bean
@ConditionalOnWebApplication:当前项目是Web项目的条件下
以上注解都是组合了@Conditional元注解，只是使用了不同的条件

### 6.5.3 实例分析
在了解了Spring Boot的运作原理和主要的条件注解后，现在来分析一个简单的Spring Boot内置的自动配置功能：http的编码配置
我们在常规项目中配置http编码的时候是在web.xml配置一个filter
```xml
<filter>
 <filter-name>encodingFilter</filter-name>
 <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
 <init-param>
  <param-name>encoding</param-name>
  <param-value>UTF-8</param-value>
</init-param>
<init-param>
  <param-name>forceEncoding</param-name>
  <param-value>true</param-value>
</init-param>
</filter>
```
自动配置要满足两个条件：
1.能配置CharacterEncodingFilter这个Bean
2.能配置encoding和forceEncoding这两个参数

**1.配置参数**
前边讲述了类型安全的配置，Spring Boot的自动配置也是基于这一点实现的这里的配置类可以在application.properties中直接设置。
```java
@ConfigurationProperties(
    prefix = "spring.http.encoding"
)
public class HttpEncodingProperties {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Charset charset;
    private Boolean force;
    private Boolean forceRequest;
    private Boolean forceResponse;
    private Map<Locale, Charset> mapping;

    public HttpEncodingProperties() {
        this.charset = DEFAULT_CHARSET;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public boolean isForce() {
        return Boolean.TRUE.equals(this.force);
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isForceRequest() {
        return Boolean.TRUE.equals(this.forceRequest);
    }

    public void setForceRequest(boolean forceRequest) {
        this.forceRequest = forceRequest;
    }

    public boolean isForceResponse() {
        return Boolean.TRUE.equals(this.forceResponse);
    }

    public void setForceResponse(boolean forceResponse) {
        this.forceResponse = forceResponse;
    }

    public Map<Locale, Charset> getMapping() {
        return this.mapping;
    }

    public void setMapping(Map<Locale, Charset> mapping) {
        this.mapping = mapping;
    }

    boolean shouldForce(HttpEncodingProperties.Type type) {
        Boolean force = type == HttpEncodingProperties.Type.REQUEST ? this.forceRequest : this.forceResponse;
        if (force == null) {
            force = this.force;
        }

        if (force == null) {
            force = type == HttpEncodingProperties.Type.REQUEST;
        }

        return force;
    }

    static enum Type {
        REQUEST,
        RESPONSE;

        private Type() {
        }
    }
}
```
在application.properties配置的时候前缀是spring.http.encoding
默认编码方式为UTF-8,若修改可使用spring.http.encoding.charset=编码
设置forceEncoding,默认为true,若修改可使用spring.http.encoding.force=false
**2.配置Bean**
通过调用上述配置，并根据条件配置CharacterEncodingFilter的Bean
```java
@Configuration
//开启属性注入，通过@EnableConfigurationProperties声明，使用@Autowired注入
@EnableConfigurationProperties({HttpEncodingProperties.class})
@ConditionalOnWebApplication
//当CharacterEncodingFilter在类路径的条件下
@ConditionalOnClass({CharacterEncodingFilter.class})
//当设置spring.http.encoding=enabled的情况下，如果没有设置则默认为true,即条件符合
@ConditionalOnProperty(
    prefix = "spring.http.encoding",
    value = {"enabled"},
    matchIfMissing = true
)
public class HttpEncodingAutoConfiguration {
    private final HttpEncodingProperties properties;

    public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
        this.properties = properties;
    }

    @Bean
    //当容器没有CharacterEncodingFilter这个Bean时创建这个Bean
    @ConditionalOnMissingBean({CharacterEncodingFilter.class})
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(this.properties.getCharset().name());
        filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
        filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
        return filter;
    }

    @Bean
    public HttpEncodingAutoConfiguration.LocaleCharsetMappingsCustomizer localeCharsetMappingsCustomizer() {
        return new HttpEncodingAutoConfiguration.LocaleCharsetMappingsCustomizer(this.properties);
    }

    private static class LocaleCharsetMappingsCustomizer implements EmbeddedServletContainerCustomizer, Ordered {
        private final HttpEncodingProperties properties;

        LocaleCharsetMappingsCustomizer(HttpEncodingProperties properties) {
            this.properties = properties;
        }

        public void customize(ConfigurableEmbeddedServletContainer container) {
            if (this.properties.getMapping() != null) {
                container.setLocaleCharsetMappings(this.properties.getMapping());
            }

        }

        public int getOrder() {
            return 0;
        }
    }
}
```
自定义一个自动配置的项目
详见spring-boot-starter-hello
1.新建starterde maven项目
pom中引入spring-boot-autoconfigure自动配置作为依赖
2.属性配置，HelloServiceProperties
3.判断依据类 HelloService
4.自动配置类HelloServiceAutoConfiguration
5.注册配置
若想自动配置生效，需要注册自动配置类。在src/main/resources下新建META-INF/spring.factories,内容如下：
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
cn.andy.springBoot.HelloServiceAutoConfiguration
```
若有多个自动配置，则用“,”隔开，此处“\”是为了换行后仍然能读到属性
到此结束

其他项目可以在pom.xml中添加spring-boot-starter-hello的依赖
在开发阶段，我们引入的依赖是spring-boot-starter-hello这个项目，在starter稳定之后，我们可以将spring-boot-starter-hello
通过“mvn install”安装到本地库，或者将这个jar包发布到Maven私服上
通过在spring-boot项目上进行测试验证发现自动配置起作用了














