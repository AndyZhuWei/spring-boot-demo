package cn.andy.springbootcache.springbootcachedemo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: zhuwei
 * @Date:2019/11/19 14:16
 * @Description:
 */
@Entity//1 @Entity注解指明这是一个和数据库表映射的实体类
public class Person {
    @Id //2@Id注解指明这个属性映射为数据库的主键
    //3 @GeneratedValue注解默认使用主键生成方式为自增，hibernate会为我们自动生成
    //一个名为HIBERNATE_SEQUENCE的序列
    @GeneratedValue
    private Long id;

    private String name;

    private Integer age;

    private String address;

    public Person(){
        super();
    }

    public Person(Long id,String name, Integer age, String address) {
        super();
        this.id=id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
