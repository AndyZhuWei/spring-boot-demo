package cn.andy.springbootredis.springbootredisdemo.dao;

import cn.andy.springbootredis.springbootredisdemo.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author: zhuwei
 * @Date:2019/11/26 13:54
 * @Description:
 */
@Repository
public class PersonDao {

    //1Spring Boot已为我们配置StringRedisTemplate，在此处可以直接注入
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //3 可以使用@Resource注解指定sringRedisTemplate，可注入基于字符串
    //的简单属性操作方法
    @Resource(name="stringRedisTemplate")
    ValueOperations<String,String> valOpsStr;

    //2 Spring Boot已为我们配置RedisTemplate，在此处可以直接注入
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    //4可以使用@Resource注解指定redisTemplate，可注入基于对象的简单属性操作方法
    @Resource(name="redisTemplate")
    ValueOperations<Object,Object> valOps;

    //5通过set方法，存储字符串类型
    public void stringRedisTemplateDemo() {
        valOpsStr.set("xx","yy");
    }

    //6通过set方法，存储对象类型
    public void save(Person person) {
        valOps.set(person.getId(),person);
    }

    //7 通过get方法，获得字符串
    public String getString() {
        return valOpsStr.get("xx");
    }

    //8 通过get方法，获得对象
    public Person getPerson() {
        return (Person)valOps.get("1");
    }
}
