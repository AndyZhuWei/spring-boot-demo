package andy.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 11:03
 * @Description: 配置类
 */
@Configuration
@ComponentScan("cn.andy.aop")
//使用@EnableAspectJAutoProxy注解开启Spring对AspectJ代理的支持
@EnableAspectJAutoProxy
public class AopConfig {
}
