package andy.el;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 11:50
 * @Description:
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ELConfig.class);

        ELConfig elConfig = context.getBean(ELConfig.class);
        elConfig.outputResource();
        context.close();
    }
}
