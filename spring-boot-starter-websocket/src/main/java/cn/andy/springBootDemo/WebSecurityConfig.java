package cn.andy.springBootDemo;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: zhuwei
 * @Date:2019/11/16 13:30
 * @Description:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
             //设置Spring Security对/和/login路径不拦截
            .antMatchers("/","/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
             //设置Spring Security的登录页面访问的路径为/login
            .loginPage("/login")
             //登录成功后转向/chat路径
            .defaultSuccessUrl("/chat")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在内存中分别配置两个用户wyf和wisely，密码和用户名一致，角色是USER
        auth.inMemoryAuthentication()
             .passwordEncoder(new BCryptPasswordEncoder())
            .withUser("wyf").password(new BCryptPasswordEncoder().encode("wyf")).roles("USER")
            .and()
            .withUser("wisely").password(new BCryptPasswordEncoder().encode("wisely")).roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //"/resources/static/"目录下的静态资源，Spring Security不拦截
        web.ignoring().antMatchers("/resources/static/**");
    }
}

























































