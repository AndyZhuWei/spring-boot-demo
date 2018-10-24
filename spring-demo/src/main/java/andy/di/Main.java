package andy.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 9:55
 * @Description:
 */
public class Main {

    public static void main(String[] args) {
        //使用AnnotationConfiguraitonContext作为Spring容器，接受输入一个配置类作为参数
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(DiConfig.class);

        UseFunctionService useFunctionService =
                context.getBean(UseFunctionService.class);

        System.out.println(useFunctionService.sayHello("di"));

        context.close();
    }
}
