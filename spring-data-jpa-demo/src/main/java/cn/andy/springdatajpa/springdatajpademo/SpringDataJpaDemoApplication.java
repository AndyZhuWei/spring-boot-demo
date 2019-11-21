package cn.andy.springdatajpa.springdatajpademo;

import cn.andy.springdatajpa.springdatajpademo.support.CustomRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//在配置类上配置@EnableJpaRepositories，并指定repositoryFactoryBeanClass
//让我们自定义的Repository实现起效
@EnableJpaRepositories(repositoryFactoryBeanClass =
        CustomRepositoryFactoryBean.class)
public class SpringDataJpaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaDemoApplication.class, args);
    }

}
