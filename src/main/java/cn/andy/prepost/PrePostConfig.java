package cn.andy.prepost;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 13:51
 * @Description:
 */
@Configuration
@ComponentScan("cn.andy.prepost")
public class PrePostConfig {

    //initMethod和destroyMethod指定BeanWayService类的init和destroy方法在构造之后Bean销毁之前执行
    @Bean(initMethod = "init",destroyMethod = "destroy")
    BeanWayService beanWayService() {
        return new BeanWayService();
    }

    @Bean
    JSR250WayService jsr250WayService() {
        return new JSR250WayService();
    }
}
