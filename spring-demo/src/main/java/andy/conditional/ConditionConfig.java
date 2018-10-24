package andy.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:31
 * @Description:
 */
@Configuration
public class ConditionConfig {

    @Bean
    //通过@Conditional注解，符合Windows条件则实例化WindowsListService
    @Conditional(WindowsCondition.class)
    public ListService windowsListService() {
        return new WindowsListService();
    }

    @Bean
    //通过@Conditional注解，符合Linux条件则实例化LinuxListService
    @Conditional(LinuxCondition.class)
    public ListService linuxListService() {
        return new LinuxListService();
    }
}
