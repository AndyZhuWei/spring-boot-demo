package cn.andy.springbootcache.springbootcachedemo.web;

import cn.andy.springbootcache.springbootcachedemo.domain.Person;
import cn.andy.springbootcache.springbootcachedemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhuwei
 * @Date: 2019/11/25 15:45
 * @Description:
 */
@RestController
public class CacheController {

    @Autowired
    DemoService demoService;

    @RequestMapping("/put")
    public Person save(Person person) {
        return demoService.save(person);
    }

    @RequestMapping("/able")
    public Person cacheable(Person person) {
        return demoService.findOne(person);
    }

    @RequestMapping("/evit")
    public String evit(Long id) {
        demoService.remove(id);
        return "ok";
    }

}
