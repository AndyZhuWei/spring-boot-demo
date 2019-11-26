package cn.andy.springbootcache.springbootcachedemo.web;

import cn.andy.springbootcache.springbootcachedemo.dao.PersonRepository2;
import cn.andy.springbootcache.springbootcachedemo.domain.Person;
import cn.andy.springbootcache.springbootcachedemo.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zhuwei
 * @Date:2019/11/19 15:20
 * @Description:
 */
@RestController
public class DataController {
    //1.Spring Data JPA已自动为你注册bean，所以可自动注入
    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonRepository2 personRepository2;
    /**
     * 保存
     * save支持批量保存：<S extends T>Iterable<S> save(Iterable<S> entities);
     *
     * 删除：
     * 支持使用id删除对象、批量删除以及删除全部
     * void delete(ID id);
     * void delete(T entity);
     * void delete(Iterable< ? extends T> entities);
     * void deleteAll();
     */
    @RequestMapping("/save")
    public Person save(String name, String address, Integer age) {
        Person p = personRepository.save(new Person(null,name,age,address));
        return p;
    }

    /**
     * 测试findByAddress
     */
    @RequestMapping("/q1")
    public List<Person> q1(String address) {
        List<Person> people = personRepository.findByAddress(address);
        return people;
    }

    /**
     * 测试findByNameAndAddress
     */
    @RequestMapping("/q2")
    public Person q2(String name,String address) {
        Person people = personRepository.findByNameAndAddress(name,address);
        return people;
    }

    /**
     * 测试withNameAndAddressQuery
     */
    @RequestMapping("/q3")
    public Person q3(String name,String address) {
        Person p = personRepository.withNameAndAddressQuery(name,address);
        return p;
    }

    /**
     * 测试withNameAndAddressNameQuery
     */
    @RequestMapping("/q4")
    public Person q4(String name,String address) {
        Person p = personRepository.withNameAndAddressNamedQuery(name,address);
        return p;
    }

    /**
     * 测试排序
     */
    @RequestMapping("/sort")
    public List<Person> sort() {
        List<Person> people = personRepository.findAll(new Sort(Sort.Direction.ASC,"age"));
        return people;
    }

    /**
     * 测试分页
     */
    @RequestMapping("/page")
    public Page<Person> page() {
        //new PageRequest(1,2)已经弃用了，使用PageRequest.of(1, 2);
//        Page<Person> pagePeople = personRepository.findAll(new PageRequest(1,2));
        //注意：我们使用的数据库是oracle11的，不支持Hibernate5原生的分页查询offset 0 rows fetch next 5 rows only
        //所以需要配置方言spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle10gDialect
        Page<Person> pagePeople = personRepository.findAll(PageRequest.of(1, 2));
        return pagePeople;
    }


    /**
     * 控制器中接受一个Person对象，当Person的name有值时，会自动对name进行like查询，当age有值时，会进行等于查询；
     * 当Person中有多个值不为空的时候，会自动构造多个查询条件，
     * 当person所有值为空的时候，默认查询出所有记录
     *
     * 需要特别注意的是：在实体类中定义的数据类型要用包装类型，而不能使用原始类型。因为在Spring MVC中，使用原始类型会自动初始化为
     * 0，而不是空，导致我们构造条件失败。
     * @param person
     * @return
     */
    @RequestMapping("/auto")
    public Page<Person> auto(Person person) {
        Page<Person> pagePeople = personRepository2.findByAuto(person,PageRequest.of(0,10));
        return pagePeople;
    }





















}
