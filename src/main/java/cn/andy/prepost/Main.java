package cn.andy.prepost;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 13:55
 * @Description:
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrePostConfig.class);

        BeanWayService beanWayService = context.getBean(BeanWayService.class);

        JSR250WayService jsr250WayService = context.getBean(JSR250WayService.class);

        context.close();
    }
}
