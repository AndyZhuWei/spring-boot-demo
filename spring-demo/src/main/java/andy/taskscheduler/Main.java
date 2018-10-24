package andy.taskscheduler;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:16
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(TaskSchedulerConfig.class);
    }
}
