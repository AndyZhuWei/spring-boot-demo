package andy.aop;

import java.lang.annotation.*;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 10:45
 * @Description: 编写拦截规则的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
    String name();
}
