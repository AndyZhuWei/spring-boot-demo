package cn.andy.springdatarest.dao;

import cn.andy.springdatarest.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @Author: zhuwei
 * @Date:2019/11/21 23:17
 * @Description:
 */
@RepositoryRestResource(path="people")
public interface PersonRepository extends JpaRepository<Person,Long> {

    @RestResource(path="nameStartsWith",rel="nameStartsWith")
    Person findByNameStartsWith(String name);
}
