package cn.andy.springboottransactioaldemo.service.impl;

import cn.andy.springboottransactioaldemo.dao.PersonRepository;
import cn.andy.springboottransactioaldemo.domain.Person;
import cn.andy.springboottransactioaldemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author: zhuwei
 * @Date:2019/11/23 11:28
 * @Description:
 */
@Service
public class DemoServiceImpl implements DemoService {

    //1.可以直接注入我们的PersonRepository的Bean
    @Autowired
    PersonRepository personRepository;

    //2使用@Transactional注解的rollbackFor属性，指定特定异常时，数据回滚
    @Transactional(rollbackFor={IllegalArgumentException.class})
    @Override
    public Person savePersonWithRollBack(Person person) {
        Person p = personRepository.save(person);
        if(person.getName().equals("汪云飞")) {
            //3硬编码手动触发异常
            throw new IllegalArgumentException("汪云飞已存在，数据将回滚");
        }
        return p;
    }

    //4使用@Transaction注解的noRollbackFor属性，指定特定异常时，数据不回滚
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    @Override
    public Person savePersonWithoutRollBack(Person person) {
       Person p = personRepository.save(person);
       if(person.getName().equals("汪云飞")) {
           throw new IllegalArgumentException("汪云飞虽已存在，数据将不会回滚");
       }
        return p;
    }

}
