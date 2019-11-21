package andy.di;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 9:52
 * @Description:
 */
//@Configuration声明当前类是一个配置类
@Configuration
//@ComponentScan，自动扫描包名下所有使用@Service、@Component、@Repository和
//@Controller的类，并注册为Bean
@ComponentScan("andy.di")
public class DiConfig {
}
