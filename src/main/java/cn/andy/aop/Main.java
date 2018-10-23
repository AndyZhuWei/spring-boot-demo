package cn.andy.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 11:05
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AopConfig.class);

        DemoAnnotationService demoAnnotationService =
                context.getBean(DemoAnnotationService.class);

        DemoMethodService demoMethodService =
                context.getBean(DemoMethodService.class);

        demoAnnotationService.add();

        demoMethodService.add();

        context.close();
    }
}
