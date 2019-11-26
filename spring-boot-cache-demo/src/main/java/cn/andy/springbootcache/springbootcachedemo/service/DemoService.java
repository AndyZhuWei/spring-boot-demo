package cn.andy.springbootcache.springbootcachedemo.service;

import cn.andy.springbootcache.springbootcachedemo.domain.Person;

/**
 * @Author: zhuwei
 * @Date:2019/11/25 14:48
 * @Description:
 */
public interface DemoService {
    public Person save(Person person);

    public void remove(Long id);

    public Person findOne(Person person);

}
