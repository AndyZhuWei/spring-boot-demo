package andy.taskscheduler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:15
 * @Description:
 */
@Configuration
@ComponentScan("cn.andy.taskscheduler")
//通过@EnableScheduling注解开启对计划任务的支持
@EnableScheduling
public class TaskSchedulerConfig {
}
