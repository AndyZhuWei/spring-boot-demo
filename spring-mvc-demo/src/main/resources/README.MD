## 4.1 Spring MVC概述
MVC:Model+View+Controller(数据模型+视图+控制器)
三层架构：Presentation tier+Application tier+Data tier(展现层+应用层+数据访问层)
MVC和三层架构的关系？
实际上MVC只存在三层架构的展现层,M实际上是数据模型，是包括数据的对象。在Spring MVC里，有一个专门的类交Model,
用来和V之间的数据交互、传值。V指的是视图页面，包含JSP\freeMarker\Velocity\Thymelead\Tile等。C当前就是
控制器（Spring MVC的注解@Controller的类）
而三层架构是整个应用的架构，是由Spring框架负责管理的。一般项目结构中都有Service层、Dao层，这两个反馈在应用层
和数据访问层

Spring MVC提供了一个DispatcherServlet来开发Web应用。在Servlet2.5及以下的时候只要在web.xml下配置<serlet>
元素即可。但我们在本节将使用Servlet3.0+无web.xml的配置方式，在Spring MVC里实现WebApplicationInitializer接口
便可实现等同于web.xml的配置

Maven标准的页面是放在src/main/webapp/WEB-INF下，此处放置在src/main/resources下是提前习惯Spring Boot的页面习惯放置方式

Spring MVC的ViewResolver,这是Spring MVC视图（JSP下就是html）渲染的核心机制：Spring MVC里有一个接口叫做ViewResoler(我们的
ViewResolver都实现该接口)，实现这个接口要重写resoleViewName(),这个方法的返回值是接口View,而View的职责就是使用model\request\response对象，
并将渲染的视图（不一定是html,可能是json、xml、pdf）返回给浏览器

**Spring中WebApplicationInitializer的理解**
现在JavaConfig配置方式在逐步取代xml配置方式。而WebApplicationInitializer可以看做是Web.xml的替代，它是一个接口。通过实现
WebApplicationInitializer，在其中可以添加servlet,listener等，在加载Web项目的时候会加载这个接口实现类，从而起到web.xml
相同的作用
```java
public interface WebApplicationInitializer {
    void onStartup(ServletContext var1) throws ServletException;
}
```
只有一个方法，看不出什么头绪。但是，在这个包下有另外一个类，SpringServletContainerInitializer
```java
@HandlesTypes({WebApplicationInitializer.class})
public class SpringServletContainerInitializer implements ServletContainerInitializer {
    public SpringServletContainerInitializer() {
    }

    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
        List<WebApplicationInitializer> initializers = new LinkedList();
        Iterator var4;
        if (webAppInitializerClasses != null) {
            var4 = webAppInitializerClasses.iterator();

            while(var4.hasNext()) {
                Class<?> waiClass = (Class)var4.next();
                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) && WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
                    try {
                        initializers.add((WebApplicationInitializer)waiClass.newInstance());
                    } catch (Throwable var7) {
                        throw new ServletException("Failed to instantiate WebApplicationInitializer class", var7);
                    }
                }
            }
        }

        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
        } else {
            AnnotationAwareOrderComparator.sort(initializers);
            servletContext.log("Spring WebApplicationInitializers detected on classpath: " + initializers);
            var4 = initializers.iterator();

            while(var4.hasNext()) {
                WebApplicationInitializer initializer = (WebApplicationInitializer)var4.next();
                initializer.onStartup(servletContext);
            }

        }
    }
}
```
先判断webAppInitializerClass这个Set是否为空。如果不为空的话，找到这个set中不是接口，不是抽象类并且是WebApplicationInitializer接口实现类的类，
将它们保存到list中。当这个list为空的时候，抛出异常。不为空的话就按照一定的顺序排序，并将它们按照一定的顺序实例化。调用其onStartup方法执行。到这里，
就可以解释WebApplicationInitializer实现类的工作过程了。但是，在web项目运行的时候，SpringServletContainerInitializer这个类又是怎样被调用
的呢？
它只有一个接口，ServletContainerInitializer，通过它就可以解释SpringServletContainerInitializer是如何被调用的。
```java
package javax.servlet;

import java.util.Set;

public interface ServletContainerInitializer {
    void onStartup(Set<Class<?>> var1, ServletContext var2) throws ServletException;
}
```
首先，这个接口是javax.servlet下的。官方的解释是这样的：为了支持可以不使用web.xml。提供了ServletContainerInitializer，它可以通过SPI机制，当
启动web容器的时候，会自动到添加的相应jar包下找到META-INF/services下以ServletContainerInitializer的全路径名称命名的文件，它的内容为ServletContainerInitializer实现类
的全路径，将它们实例化。既然这样的话，那么SpringServletContainerInitializer作为ServletContainerInitializer的实现类，它的jar包下也应该有相应的文件
`ServletContainerInitializer.png`
哈，现在就可以解释清楚了。首先，SpringServletContainerInitializer作为ServletContainerInitializer的实现类，通过SPI机制，在web容器加载的时候
会自动的被调用。（这个类上还有一个注解@HandlesTypes,它的作用是将感兴趣的一些类注入到ServletContainerInitializer）,而这个类的方法又会扫描找到
WebApplicationIitializer的实现类，调用它的onStartup方法，从而起到启动web.xml相同的作用。


##4.4Spring MVC基本配置
Spring MVC的定制配置需要我们配置类继承一个WebMvcConfigurerAdapter类，并在此类使用@EnableWebMvc注解，来开启对
Spring MVC的配置支持，这样我们就可以重写这个类的方法，完成我们的常用配置

### 4.1.1 静态资源映射
程序的静态文件（js\css\图片）等需要直接访问，这时我们可以在配置里重写addResourceHandlers方法来实现。
@EnableWebMvc开启SpringMVC支持，若无此句，重写WebMvcConfigurerAdapter方法无效
继承WebMvcConfigurerAdapter类，重写其方法可对Spring MVC进行配置
```java
//静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceLocations值的是文件放置的目录
        //addResourceHandler指的是对外暴露的访问路径
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");
    }
```

### 4.4.2 拦截器配置
拦截器（Interceptor）实现对每一个请求处理前后进行相关的业务处理，类似于Servlet的Filter
可让普通的Bean实现HanlderInterceptor接口或继承HandlerInterceptorAdapter类来实现自定义蓝机器
通过重写WebMvcConfigurerAdpater的addInterceptors方法来注册自定义的拦截器
```java
//继承HandlerInterceptorAdapter类来实现自定义拦截器
public class DemoInterceptor extends HandlerInterceptorAdapter{

    //重写preHandle方法，在请求发生前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       long startTime = System.currentTimeMillis();
       request.setAttribute("startTime",startTime);
       return true;
    }

    //重写postHandler方法，在请求完成后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long)request.getAttribute("startTime");
        request.removeAttribute("startTime");
        long endTime = System.currentTimeMillis();
        System.out.println("本次请求处理时间为："+new Long(endTime-startTime)+"ms");
        request.setAttribute("handlingTime",endTime-startTime);
    }
}
```

```java
//配置拦截器的Bean
    @Bean
    public DemoInterceptor demoInterceptor() {
        return new DemoInterceptor();
    }

    //重写addInterceptors方法，注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(demoInterceptor());
    }
```

### 4.4.3@ControllerAdvice
通过@ControllerAdvice，我们可以将对于控制器的全局配置放置在同一个位置，注解了@Controller的类的方法可使用
@ExceptionHandler、@InitBinder、@ModelAttribute注解到方法上。
@ExceptionHandler：用于全局处理控制器里的异常
@InitBinder：用来设置WebDataBinder,WebDataBinder用来自动绑定前台请求参数到Model中
@ModelAttribute：@ModelAttribute本来的作用是绑定键值对到Model里，此处是让全局的@RequstMapping都能获得在此处设置的键值对

### 4.4.4 其他配置
1. 快捷的ViewController
在前面我们写了如下的代码
```java
 @RequestMapping("/index")
    public String hello() {
        return "index";
    }
```
此处无任何业务处理，只是简单的页面转向，写了至少三行有效代码；在实际开发中会设计大量这样的页面转向，若都这样写会很麻烦，我们可以通过在
配置中重写addViewControllers来简化配置：
```java
//配置VeiwController
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/toUpload").setViewName("/upload");
        registry.addViewController("/converter").setViewName("/converter");
        registry.addViewController("/sse").setViewName("/sse");
        registry.addViewController("/async").setViewName("/async");
    }
```
2.路径匹配参数配置
在Spring MVC中，路径参数如果带“.”的话，“.”后面的值将被忽略，录入，访问
http://localhost:8080/anno/pathvar/xx.yy，此时“.”后面的yy被忽略，通过重写configurePathMatch方法可不忽略"."后面的参数
```java
 //配置路径匹配参数
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
```
3.更多配置
参见WebMvcConfigurerAdapter类的API.因其是WebMvcConfigurer接口的实现，所以WebMvcConfigurer的API内的方法也可以用来配置MVC

## 4.5Spring MVC的高级配置
### 4.5.1 文件上传配置
Spring MVC通过配置一个MultipartResolver来上传文件。
在Spring的控制器中，通过MultipartFile file来接收文件，通过MultipartFile[] files接收多个文件上传
multipartResolver配置
```java
@Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }
```

### 4.5.2自定义HttpMessageConverter
HttpMessageConverter是用来处理request和response里的数据的。Spring为我们内置了大量的HttpMessageConverter,
例如MappingJackson2HttpMessageConverter、StringHttpMessageConverter等。
自定义详见MyMessageConverter

### 4.5.3 服务器端推送技术
早期解决方案是使用Ajax向服务器轮询消息，使浏览器尽可能第一时间获得服务端的消息，因为这种方式的轮询频率不好控制，所以大大
增加了服务端的 压力
本节所有的服务端推送方案都是基于：当客户端向服务端发送请求，服务端会抓住这个请求不放，等有数据更新的时候才返回给客户端，
当客户端接收到消息后，再向服务端发送请求，周而复始。这种方式的好处是减少了服务器的请求数量，大大减少了服务器的压力
除了服务器端推送的技术以外，还有一个另外的双向通信的技术——WebSocket。在第三部分Spring Boot中演示
本节将提供基于SSE(Server Send Event服务端发送事件)的服务端推送和基于Servlet3.0+的异步方法特性，其中第一种方式
需要新式浏览器的支持，第二种方式是跨浏览器的
1.SSE
详见SseController
2.Servlet 3.0+异步方法处理
```java
public class WebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //新建WebApplicationContext,注册配置类，并将其和当前servletContext关联
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(MyMvcConfig.class);
        ctx.setServletContext(servletContext);
        //注册Spring MVC的DispatcherServlet
        Dynamic servlet = servletContext.addServlet("dispatcher",new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
        //开启异步方法支持
        servlet.setAsyncSupported(true);
    }
}
```
详见AysncController

## 4.6Spring MVC的测试
为了测试Web项目通常不需要启动项目，我们需要一些Servlet相关的模拟对象，比如：MockMVC、MockHttpServletRequest、
MockHttpServletResponse、MockHttpSession等
在Spring里，我们使用@WebAppConfiguration指定加载的ApplicationContext是一个WebApplicationContext
详见TestControllerIntegrationTests






















