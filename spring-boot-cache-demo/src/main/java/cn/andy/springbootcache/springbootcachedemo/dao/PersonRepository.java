package cn.andy.springbootcache.springbootcachedemo.dao;

import cn.andy.springbootcache.springbootcachedemo.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhuwei
 * @Date:2019/11/19 15:05
 * @Description:
 */
public interface PersonRepository extends JpaRepository<Person,Long> {
}
