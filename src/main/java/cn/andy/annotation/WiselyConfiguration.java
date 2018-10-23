package cn.andy.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//组合@Configuration元注解
@Configuration
//组合@ComponentScan元注解
@ComponentScan
public @interface WiselyConfiguration {
    //覆盖value参数
    String[] value() default {};
}
