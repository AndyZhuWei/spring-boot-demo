package cn.andy.springboottransactioaldemo.service;

import cn.andy.springboottransactioaldemo.domain.Person;

/**
 * @Author: zhuwei
 * @Date:2019/11/23 11:26
 * @Description:
 */
public interface DemoService {
    public Person savePersonWithRollBack(Person person);
    public Person savePersonWithoutRollBack(Person person);
}
