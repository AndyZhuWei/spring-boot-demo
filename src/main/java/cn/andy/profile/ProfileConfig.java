package cn.andy.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 14:12
 * @Description:
 */
@Configuration
public class ProfileConfig {

    @Bean
    //Profile为dev时实例化devDemoBean
    @Profile("dev")
    public DemoBean devDemoBean() {
        return new DemoBean("from development profile");
    }

    @Bean
    //Profile为prod时实例化prodDemoBean
    @Profile("prod")
    public DemoBean prodDemoBean() {
        return new DemoBean("from production profile");
    }
}
