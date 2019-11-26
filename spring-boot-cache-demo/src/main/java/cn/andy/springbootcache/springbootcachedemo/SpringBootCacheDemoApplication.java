package cn.andy.springbootcache.springbootcachedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootCacheDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCacheDemoApplication.class, args);
    }

}
