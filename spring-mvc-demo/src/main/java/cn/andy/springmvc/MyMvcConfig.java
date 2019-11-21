package cn.andy.springmvc;

import cn.andy.springmvc.interceptor.DemoInterceptor;
import cn.andy.springmvc.messageconverter.MyMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 10:10
 * @Description:
 */
@Configuration
//@EnableWebMvc注解会开启一些默认配置，如一些ViewResolver或者MessageConverter等,
//如果没有此句，重写WebMvcConfigurerAdapter方法无效
@EnableWebMvc
@EnableScheduling
@ComponentScan("cn.andy.springmvc")
//继承WebMvcConfigurationAdapter类，重写其方法可对Spring MVC进行配置
public class MyMvcConfig extends WebMvcConfigurerAdapter{

    //配置JSP模板引擎
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //此处看到的页面效果是运行时而不是开发时的代码，运行是代码会将我们的页面自动编译到/WEB-INFO/classes/views下
        viewResolver.setPrefix("/WEB-INF/classes/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }

    //静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceLocations值的是文件放置的目录
        //addResourceHandler指的是对外暴露的访问路径
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");
    }

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

    //配置VeiwController
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/toUpload").setViewName("/upload");
        registry.addViewController("/converter").setViewName("/converter");
        registry.addViewController("/sse").setViewName("/sse");
        registry.addViewController("/async").setViewName("/async");
    }

    //配置路径匹配参数
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    //仅添加一个自定义的HttpMessageConverter，不覆盖默认注册的HttpMessageConverter
    //如果重载configureMessageConverters会重载掉Spring MVC默认注册的多个HttpMessageConverter
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }


    @Bean
    public MyMessageConverter converter() {
        return new MyMessageConverter();
    }

    /*
    Thymeleaf与Spring MVC集成，例子中AbstractConfigurableTemplateResolver为TemplateResolver
    但是TemplateResolver找不到


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
    }*/

}
