package cn.andy.springbootcache.springbootcachedemo.service.impl;

import cn.andy.springbootcache.springbootcachedemo.dao.PersonRepository;
import cn.andy.springbootcache.springbootcachedemo.domain.Person;
import cn.andy.springbootcache.springbootcachedemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: zhuwei
 * @Date:2019/11/25 14:50
 * @Description: 这里特别说明下，如果没有指定key，则方法参数作为key保存到缓存中
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    PersonRepository personRepository;

    @Override
    //1@CachePut缓存新增的或更新的数据到缓存，其中缓存名称为people,数据的key是person的id
    @CachePut(value="people",key="#person.id")
    public Person save(Person person) {
        Person p = personRepository.save(person);
        System.out.println("为id、key为："+p.getId()+"数据做了缓存");
        return p;
    }

    @Override
    //2@CacheEvict从缓存people中删除key为id的数据
    @CacheEvict(value="people")
    public void remove(Long id) {
        System.out.println("删除了id、key为"+id+"的数据缓存");
        personRepository.deleteById(id);
    }

    @Override
    //3@Cacheable缓存key为person的id数据到缓存people中
    @Cacheable(value="people",key="#person.id")
    public Person findOne(Person person) {
        Example<Person> example = Example.of(person);
        Optional<Person> personOptional = personRepository.findOne(example);

        if (personOptional.isPresent()) {
            Person personResult =   personOptional.get();
            System.out.println("为id、key为："+personResult.getId()+"数据做了缓存");
            return personResult;
        } else {
            System.out.println("no exit!");
        }
        return null;
    }

}















































