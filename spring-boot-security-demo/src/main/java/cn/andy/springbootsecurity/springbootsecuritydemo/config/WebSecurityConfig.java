package cn.andy.springbootsecurity.springbootsecuritydemo.config;

import cn.andy.springbootsecurity.springbootsecuritydemo.security.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @Author: zhuwei
 * @Date:2019/11/27 16:31
 * @Description:
 */
//1扩展Spring Security配置需继承WebSecurityConfigurerAdapter
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //2注册CustomUserService的Bean
    @Bean
    UserDetailsService customUserService() {
       return new CustomUserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //3添加我们自定义的user detail service认证
        auth.userDetailsService(customUserService()).passwordEncoder(NoOpPasswordEncoder.getInstance());
                //测试的时候可以不使用加密算法，但是实际线上要用加密算法，比如.passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()//4所有请求需要认证即登录后才能访问
                .and()
                .formLogin()
                     .loginPage("/login")
                     .failureUrl("/login?error")
                     .permitAll()//5定制登录行为，登录页面可任意访问
                .and()
                .logout().permitAll();//6定制注销行为，注销请求可任意访问
    }
}
