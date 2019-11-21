### 7.2.4 实战
1.新建Spring Boot项目
选择Thymeleaf依赖，会自动包含spring-boot-starter-web
2.示例JavaBean
新建Person
3.脚本样式静态文件
根据默认原则，脚本样式、图片等静态文件应放置在src/main/resources/static下，这里引入了Bootstrap和jQuery
4.演示页面
根据默认原则，页面应放置在src/main/resources/templates下，在这个目录下新建index.html
5.数据准备
```java
@RequestMapping("/index")
	public String index(Model model) {
		Person single = new Person("aa",11);
		List<Person> people = new ArrayList<Person>();
		Person p1 = new Person("xx",11);
		Person p2 = new Person("yy",22);
		Person p3 = new Person("zz",33);
		people.add(p1);
		people.add(p2);
		people.add(p3);

		model.addAttribute("singlePerson",single);
		model.addAttribute("people",people);

		return "index";

	}
```
6.运行
访问http://localhost:8080/
注意：
`
改了好几个小时，总是不生效，实验了各种写法，终于搞清楚了。使用spingboot 2.1.1 (使用的就是)以上的都要注意了。

THYMELEAF 3.0.10更改日志
*改进了HTML事件处理程序属性（th：on ），以允许将其值作为内联JavaScript的片段（使用JAVASCRIPT模板模式）进行处理。

以前写法(请放弃)：
方式一：
<button class="btn" th:onclick="'getName(\'' + ${person.name} + '\');'">获得名字</button>
方式二：
<button class="btn" th:onclick="'getName(' + ${person.name} + ');'">获得名字</button>
方式三：
<button th:onclick="|getName(${person.name} )|">获得名字</button>

现在的写法：
<button class="btn" th:onclick="getName([[${person.name}]]);">获得名字</button>
`
```java
public static void main(String[] args) {
        char c1 = 'ο';
        char c2 = 'o';
        int i1 = (int)c1;//959
        int i2 = (int)c2;//111
        System.out.println(i1);
        System.out.println(i2);
        //c1与c2不相等
    }
```

## 7.3 Web相关配置
### 7.3.1 Spring Boot提供的自动配置
通过查看WebMvcAutoConfiguration及WebMvcProperties的源码，可以发现Spring Boot为我们提供了如下的自动配置。
1.自动配置的ViewResolver
(1)ContentNegotiatingViewResolver
这是Spring MVC提供的一个特殊的ViewResolver,ContentNegotiatingViewResolver不是自己处理View,而是代理给不同的ViewResolver
里处理不同的View,所以它有最高的优先级
(2)BeanNameViewResolver
在控制器（@Controller）中的一个方法的返回值的字符串（视图名）会根据BeanNameViewResolver去查找Bean的名称为返回字符串的View来渲染视图。
例如：
定义BeanNameViewResolver的Bean:
```java
@Bean
public BeanNameViewResolver beanNameViewResolver() {
    BeanNameViewResolver resolver = new BeanNameViewResolver();
    return resolver;
}
```
定义一个View的Bean,名称为jsonView
```java
@Bean
public MappingJackson2JsonView jsonView() {
    MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
    return jsonView;
}
```
在控制器中，返回值为字符串jsonView，它会找Bean的名称为jsonView的视图来渲染
```java
@RequestMapping(value="/json",produces={MediaType.APPLICATION_JSON_VALUE})
public String json(Model model) {
    Person single = new Person("aa",11);
    model.addAttribute("single",single);
    return "jsonView"
}
```
(3)InternalResourceViewResolver
这个是一个极为常用的ViewResoler,主要通过设置前缀、后缀，以及控制器中方法来返回视图名的字符串，以得到实际页面，Spring Boot的源码如下：
```java
@Bean
@ConditionalOnMissingBean(InternalResourceViewResolver.class)
public InternalResourceViewResolver defaultViewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix(this.prefix);
    resolver.setSuffix(this.suffix);
    return resolver;
}
```
2. 自动配置的静态资源
在自动配置类的addResourceHandlers方法中定义了以下静态资源的自动配置
(1)类路径文件
把类路径下的/static、/public、/resources和/META-INF/resources文件夹下的静态文件直接映射为/**,可以通过http://localhost:8080/**来访问
(2)webjar
何谓webjar,webjar就是将我们常用的脚本框架封装在jar包中的jar包，更多关于webjar的内容请访问http://www.webjars.org网站
把webjar的/META-INF/resources/webjars/下的静态文件映射为/webjar/**,可以通过http://localhost:8080/webjar/**来访问
3.自动配置的Formatter和Converter
关于自动配置Formatter和Converter，我们可以看一下WebMvcAutoConifguration类中的定义：
```java
@Override
		public void addFormatters(FormatterRegistry registry) {
			for (Converter<?, ?> converter : getBeansOfType(Converter.class)) {
				registry.addConverter(converter);
			}
			for (GenericConverter converter : getBeansOfType(GenericConverter.class)) {
				registry.addConverter(converter);
			}
			for (Formatter<?> formatter : getBeansOfType(Formatter.class)) {
				registry.addFormatter(formatter);
			}
		}
		
		private <T> Collection<T> getBeansOfType(Class<T> type) {
        			return this.beanFactory.getBeansOfType(type).values();
        		}
```
从代码中可以看出，只要我们定义了Converter、GenericConverter和Formatter接口的实现类的Bean,这些Bean就会自动注册到Spring MVC中。
4.自动配置的HttpMessageConverters
在WebMvcAutoConfiguration中，我们注册了messageConverters，代码如下
```java
@Autowired
private HttpMessageConverters messageConverters;

@Override
public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.addAll(this.messageConverters.getConverters());
}
```
在这里直接注入了HttpMessageConverters的Bean，而这个Bean是在HttpMessageConvertersAutoConfiguration类中定义的，我们自动注册的HttpMessageConverter
除了Spring MVC默认的ByteArrayHttpMessageConverter、StringHttpMessageConverter、ResourceHttpMessageConverter、SourceHttpMessageConverter\
AllEncompassingFormHttpMessageConverter外，在我们的HttpMessageConvertersAutoConiguration的自动配置文件里还引入了JacksonHttpMessageConvertersConfiguration和
GsonHttpMessageConverterConfiguratin，使我们获得了额外的HttpMessageConverter
在Spring Boot中，如果要新增自定义的HttpMessageConverter,则只需定义一个你自己的HttpMessageConverters的Bean,然后在此Bean中注册自定义HttpMessageConverter即可。
```java
@Bean
public HttpMessageConverters customConverters() {
    HttpMessageConverter<?> customConverter1 = new CustomConverter1();
    HttpMessageConverter<?> customConverter2 = new CustomConverter2();
    return new HttpMessageConverters(customConverter1,customConverter2);
}
```
5.静态首页的支持
把静态index.html文件放置在如下目录，
* classpath:/META-INF/resources/index.html
* classpath:/resources/index.html
* classpath:/static/index.html
* classpath:/public/index.html
当我们访问应用根目录http://localhost:8080/时，就直接映射
### 7.3.2 接管Spring Boot的Web配置
如果Spring Boot提供的Spring MVC不符合要求，则可以通过一个配置类（注解有@Configuration的类）加上@EnableWebMvc注解来实现完全自己控制的MVC配置
当然，通常情况下，Spring Boot的自动配置是符合我们大多数需求的。在你既需要保留Spring Boot提供的便利，又需要增加自己的额外的配置的时候，可以定义一个配置类
并继承WebMvcConfigurerAdapter，无须使用@EnableWebMvc注解，然后安装第4章讲解的Spring MVC的配置方法来添加Spring Boot为我们所做的其他配置
```java
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/xx").setViewName("/xx");
    }
}
```
值得指出的是，在这里重写的addViewControllers方法，并不会覆盖WebMvcAutoConfiguration中的addViewControllers（在此方法中，Spring Boot将“/”
映射至index.html），这也就意味着我们自己的配置和Spring Boot的自动配置同时有效，这也是我们添加自己的MVC配置的方式。

### 7.3.3 注册Servlet、Filter、Listener
当使用嵌入式的Servlet容器(Tomcat、Jetty等)时，我们通过将Servlet、Filter和Listener声明为Spring Bean而达到注册的效果；或者注册
ServletRegistrationBean、FilterRegistrationBean和ServletListenerRegistrationBean的bean
(1)直接注册Bean示例
```java
@Bean
public XxServlet xxServlet() {
    return new XxServlet();
}

@Bean
public YyFilter yyFilter() {
    return new YyFilter();
}

@Bean
public ZzListener zzListener() {
    return new ZzListener();
}
```
(2)通过RegistratinBean示例
```java
@Bean
public ServletRegistrationBean servletRegistrationBean() {
    return new ServletRegistrationBean(new XxServlet(),"/xx/*");
}

@Bean
public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new YyFilter());
    registrationBean.setOrder(2);
    return registrationBean; 
}

@Bean
public ServletListenerRegistrationBean<ZzListener> zzLIstenerServletRegistrationBean() {
    return new ServletListenerRegistrationBean<ZzListener>(new ZzListener());
}
```

## 7.4Tomcat配置
本节虽然叫Tomcat配置，但其实指的是servlet容器的配置，因为Spring Boot默认内嵌的Tomcat为servlet容器，
所以本节只讲对Tomcat配置，其实对于其他容器也都是通用的。
### 7.4.1 配置Tomcat
关于Tomcat的所有属性都在org.springframework.boot.autoconfigfure.web.ServerProperties配置类中做了定义，我们只需要在application.properties
配置属性做配置即可。通用的Servlet容器配置都以“server”作为前缀，而Tomcat特有配置都以“server.tomcat”作为前缀
**配置Servlet容器**
server.port=#配置程序端口，默认为8080
server.session-timeout=#用户会话session过期时间，以秒为单位
server.context-path=#配置访问路径，默认为/
**配置Tomcat**
server.tomcat.uri-encoding=#配置Tomcat编码，默认为UTF-8
server.tomcat.compression=#Tomcat是否开启压缩，默认为关系off
### 7.4.2代码配置Tomcat
如果需要通过代码方式配置servlet容器，则可以注册一个实现EmbeddedServletContainerCustomizer接口的Bean
如果直接配置相应的容器，则可以直接指定定义
Tomcat:TomcatEmbeddedServletContainerFactory
Jetty:JettyEmbeddedServletContainerFactory
Undertow:UnderTowEmbeddedServletContainerFactory

### 7.4.3 替换Tomcat
Spring Boot默认使用Tomcat作为内嵌Servlet容器，查看spring-boot-starter-web依赖即可看到
如果要使用其他的只需要修改spring-boot-starter-web的依赖即可

### 7.4.4 SSL配置
SSL(Secure Sockets Layer，安全套接层)是为网络通信提供安全及数据完整性的一种安全协议，SSL在网络传输层对网络连接进行加密。SSL协议位于TCP/IP
协议与各种应用层协议之间，为数据通信通过安全支持。SSL协议可分为两层：SSL记录协议（SSL Record Protocol）,它建立在可靠的传输层协议（TCP）之上，
为高层协议提供数据封装、压缩、加密等基本功能的支持。SSL握手协议（SSL Handshake Protocol），它建立在SSL记录协议之上，用于在实际数据传输开始前，
通信双方进行身份认证、协商加密算法、交换加密密钥等
而在基于B/S的Web应用中，是通过HTTPS来实现SSL的。HTTPS是以安全为目标的HTTP通道，简单讲是HTTP的安全版，即在HTTP下计入SSL层，HTTPS的安全基础是
SSL.
因为Spring Boot用的是内嵌的Tomcat,因而我们做SSL配置的时候需要做如下的操作。
1.生成证书
使用SSL首先需要一个证书，这个证书既可以是自签名的，也可以是从SSL证书授权中心获得的。本例为了演示方便，使用自授权证书
每一个JDK或JRE里都有一个工具交keytool,它是一个证书管理工具，可以用来生成自签名的证书
在控制台输入如下命令，然后安装提示操作
keytool -genkey -alias tomcat -keyalg RSA -keystore E:/tomcat.keystore
按照要求执行后，就会在用户目录下生成一个.keystore文件，这就是我们要用的证书文件
（密钥为111111）
2.Spring Boot配置SSL
添加一个index.html到src/main/resources/static下，作为测试
将.keystore文件复制到项目的根目录，然后在applicatin.properties中做如下SSL的配置：
```properties
server.port=8443
server.ssl.key-store=classpath:tomcat.keystore
server.ssl.key-store-password=111111
server.ssl.keyStoreType=JKS
server.ssl.keyAlias:tomcat
```
此时启动Spring Boot，控制台输出效果如下
```text
Tomcat started on port(s): 8443 (https) with context path ''
```
3.http转向https
当我们输入http自动转向到https
要实现这个功能，我们需配置TomcatEmbeddedServletContainerFactory，并且添加Tomcat的connect来实现
这时我们需要在配置文件里增加如下配置：
详见SpringBootStaarterThymeleafApplication
此时启动Spring Boot，控制台输出效果如下
```text
Tomcat started on port(s): 8443 (https) 8080 (http) with context path ''
```
## 7.5Favicon配置
### 7.5.1 默认的Favicon
Spring Boot提供了一个默认的Favicon，每次访问应用的时候都能看到
### 7.5.2关闭Favicon
我们可以在applicatin.properties中设置关闭Favicon，默认为开启
spring.mvc.favicon.enabled=false
### 7.5.3 设置自己的Favicon
若需要设置自己的Favicon，则只需将自己的favicon.ico(文件名不能变)文件放置在类路径根目录、类路径META-INF/resources/下、
类路径static/下或类路径public下。


























