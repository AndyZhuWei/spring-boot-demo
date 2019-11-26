package cn.andy.springboottransactioaldemo.dao;


import cn.andy.springboottransactioaldemo.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhuwei
 * @Date:2019/11/21 23:17
 * @Description:
 */
public interface PersonRepository extends JpaRepository<Person,Long> {
}
