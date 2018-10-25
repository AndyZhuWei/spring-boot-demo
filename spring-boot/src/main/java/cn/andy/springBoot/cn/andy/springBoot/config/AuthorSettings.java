package cn.andy.springBoot.cn.andy.springBoot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: zhuwei
 * @Date:2018/10/25 17:38
 * @Description:
 */
@Component
//通过@ConfigurationProperties加载properties文件的配置，prefix属性指定properties的配置的前缀，
//通过locations指定properties文件的位置
@ConfigurationProperties(prefix = "author")
public class AuthorSettings {
    private String name;
    private Long age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
