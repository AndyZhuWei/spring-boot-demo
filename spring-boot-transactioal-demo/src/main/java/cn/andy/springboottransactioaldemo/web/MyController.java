package cn.andy.springboottransactioaldemo.web;

import cn.andy.springboottransactioaldemo.domain.Person;
import cn.andy.springboottransactioaldemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhuwei
 * @Date:2019/11/23 11:37
 * @Description:
 */
@RestController
public class MyController {

    @Autowired
    DemoService demoService;

    //1.测试回滚情况
    @RequestMapping("/rollback")
    public Person rollback(Person person) {
        return demoService.savePersonWithRollBack(person);
    }

    //2测试不回滚情况
    @RequestMapping("/norollback")
    public Person noRollback(Person person) {
       return demoService.savePersonWithoutRollBack(person);
    }




















}
