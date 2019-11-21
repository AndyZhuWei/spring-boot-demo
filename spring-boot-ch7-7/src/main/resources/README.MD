# 第7章 Spring Boot的Web开发
Web开发的核心内容主要包括内嵌Servlet容器和Spring MVC
## 7.1 Spring Boot的Web开发支持
Spring Boot提供了spring-boot-starter-web为Web开发予以支持，spring-boot-starter-web为我们提供了嵌入的Tomcat以及Spring MVC的依赖。
而Web相关的自动配置存储在spring-boot-autoconfigure.jar的org.springframework.boot.autoconfigure.web下，大概有如下配置
*ServerPropertiesAutoConfiguration和ServerProperties自动配置内嵌Servlet容器
*HttpEncodingAutoConfiguration和HttpEncodingProperties用来自动配置http的编码
*MultipartAutoConfiguration和MultipartProperties用来自动配置上传文件的属性
*JacksonHttpMessageConvertersConfiguration用来自动配置mappingJackson2HttpMessageConverter和mappingJackson2XmlHttpMessageConverter
*WebMvcAutoConfiguration和WebMvcProperties配置Spring MVC

## 7.2Thymeleaf模板引擎
在内嵌的Servlet的容器上运行有一些问题（内嵌Tomcat、Jetty不支持以jar形式运行JSP,Undertow不支持JSP）
Spring Boot提供了大量模板引擎，包含FreeMarker\Groovy\Thymeleaf\Velocity和Mustache,Spring Boot中推荐使用Thymeleaf
作为模板引擎，因为Thymeleaf提供了完美的Spring MVC的支持
### 7.2.1 Thymeleaf基础知识
Thymeleaf是一个Java类库，它是一个xml/xhtml/html5的模板引擎，可以作为MVC的Web应用的View层
Thymeleaf还提供了额外的模块与Spring MVC集成，所以我们可以使用Thymeleaf完全替代JSP
使用步骤:
1.引入Thymeleaf
```html
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta content="text/html;charset="UTF-8">
<!--css-->
<link th:href="@{/bootstrap/css/bootstrap.min.css}" rel="stylesheet"/>
<link th:href="@{/bootstrap/css/bootstrap-theme.min.css}" rel="stylesheet"/>
<!--javascript-->
<script th:src="@{jquery-1.10.2.min.js}" type="text/javascript"></script>
<script th:src="@{bootstrap/js/bootstrap.min.js}"></script>
</head>
<body>
</body>
</html>
```
通过xmlns:th="http://www.thymeleaf.org"命名空间，将镜头页面转换为动态的视图。需要进行动态处理的元素将使用“th:”为前缀
通过“@{}”引用Web静态资源，这在JSP下是极易出错的
2.访问model中的数据
通过“${}”访问model中的属性，这和JSP极为相似
```html
<div class="panel panel-primary">
  <div class="panel-heading">
    <h3 class="panel-title">访问model</h3>
  </div>
  <div class="panel-body">
   <span th:text="${singlePerson.name}"></span>
</div>
</div>
```
使用<span th:text="${singlePerson.name}"></span>访问model中的singlePerson的name属性。注意：需要处理的动态内容需要加上“th:”前缀

3.model中国的数据迭代
Thymeleaf的迭代和JSP的写法也很相似，代码如下：
```html
<div class="panel panel-primary">
  <div class="panel-heading">
   <h3 class="panel-title">列表</h3>
</div>
<div class="panel-body">
<ul class="list-group">
<li class="list-group-item" th:each="person:${people}">
<span th:text="${person.name}"></span>
<span th:text="${person.age}"></span>
</li>
</ul>
</div>
</div>
```
使用th:each来做循环迭代(th:each="person:${people}"),person作为迭代元素来使用，然后像上面一样访问迭代元素中的属性
4.数据判断
```html
<div th:if="${not #lists.isEmpty(people)}">
<div class="panel-primary">
<div class="panel-heading">
<h3 class="panel-title">列表</h3>
</div>
<div class="panel-body">
<ul class="list-group">
<li class="list-group-item" th:each="person:${people}">
<span th:text="${person.name}"></span>
<span th:text="${person.age}"></span>
</li>
</ul>
</div>
</div>
</div>
```
通过${not #lists.isEmpty(people)}表达式判断people是否为空。Thymeleaf支持>、<、>=、<=、==、!=作为比较条件，同时也支持将SpringEL
表达式语言用于条件中。
5.在JavaScript中访问model
在项目中，我们经常需要在JavaScript访问model中的值，在Thymeleaf里实现代码如下：
```html
<script th:inline="javascript">
var single = [[${singlePerson}]];
console.log(single.name+"/"+single.age)
</script>
```
通过th:inline="javascript"添加到script标签,这样javaScript代码即可访问model中的属性
通过“[[${}]]”格式获得实际的值。
还有一种是需要在html的代码里访问model中的属性，例如，我们需要在列表后单击每一行后面的按钮获得model中的值，可做如下处理：
```html
<li class="list-group-item" th:each="person:${people}">
<span th:text="${person.name}"></span>
<span th:text="${person.age}"></span>
<button class="btn" th:onclick="'getName(\''+${person.name}+'\');'">获得名字</button>
</li>
```
注意格式th:onclick="'getName(\''+${person.name}+'\');'"。
6其他知识
更多更完整的Thymeleaf的知识，请查看http://www.thymeleaf.org的官网

### 7.2.2 与Spring MVC集成
在Spring MVC中，若我们需要集成一个模板引擎的话，需要定义ViewResolver,而ViewResolver需要定义一个View。如前面我们为JSP定义的ViewResolver代码
```java
@Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/classes/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }
```
通过上面的代码可以看出，使用JstlView定义了一个InternalResourceViewResolver,因而使用Thymeleaf作为我们的模板引擎也应该做类似的定义。庆幸的是，
Thymeleaf为我们定义好了org.thymeleaf.spring4.view.ThymeleafView和org.thymeleaf.spring4.view.ThymeleafViewResolver(默认使用
ThymeleafView作为View).Thymeleaf给我们提供了一个SpringTemplateEngine类，用来驱动在Spring MVC下使用Thymeleaf模板引擎，另外还提供了一个
TemplateResolver用来设置通用的模板引擎（包含前缀、后缀等），这使我们在Spring MVC中集成Thymeleaf引擎变得十分简单。
```java
//Thymeleaf与Spring MVC集成，例子中AbstractConfigurableTemplateResolver为TemplateResolver
  //  但是TemplateResolver找不到
    @Bean
    public AbstractConfigurableTemplateResolver templateResolver() {
        AbstractConfigurableTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
    //    thymeleafViewResolver.setViewClass(ThymeleafView.class);
        return thymeleafViewResolver;
    }
```
注意：例子中的TemplateResolver并未找到，找到类似的一个类AbstractConfigurableTemplateResolver

### 7.2.3 Spring Boot的Thymeleaf支持
在Spring Boot中这一切都是不需要的，Spring Boot通过org.springframework.boot.autoconfigure.thymeleaf包对
Thymeleaf进行了自动配置。
   通过ThymeleafAutoConfiguration类对集成所需要的Bean进行自动配置，包括ITemplateResolver\templateEngine和
thymeleafViewResolver的配置
   通过ThymeleafProperties来配置Thymeleaf，在application.properties中，以spring.thymeleaf开头来配置，通过查看ThymeleafProperties
的主要源码，我们可以看出如何设置属性以及默认配置：
```java
@ConfigurationProperties(
    prefix = "spring.thymeleaf"
)
public class ThymeleafProperties {
    private static final Charset DEFAULT_ENCODING;
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    private boolean checkTemplate = true;
    private boolean checkTemplateLocation = true;
    //前缀设置，Spring Boot默认模板，防止在classpath:/templates/目录下
    private String prefix = "classpath:/templates/";
    //后缀设置，默认为html
    private String suffix = ".html";
    //模板模式设置，默认为HTML5
    private String mode = "HTML";
    ...
```
实战项目详见spring-boot-starter-thymeleaf













































