package cn.andy.springbootredis.springbootredisdemo.web;

import cn.andy.springbootredis.springbootredisdemo.dao.PersonDao;
import cn.andy.springbootredis.springbootredisdemo.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhuwei
 * @Date:2019/11/26 14:40
 * @Description:
 */
@RestController
public class DataController {

    @Autowired
    PersonDao personDao;

    //1演示设置字符及对象
    @RequestMapping("/set")
    public void set() {
        Person person = new Person("1","wyf",32);
        personDao.save(person);
        personDao.stringRedisTemplateDemo();
    }

    //2 演示获得字符
    @RequestMapping("/getStr")
    public String getStr() {
        return personDao.getString();
    }

    //3 演示获得对象
    @RequestMapping("/getPerson")
    public Person getPerson() {
        return personDao.getPerson();
    }

}
