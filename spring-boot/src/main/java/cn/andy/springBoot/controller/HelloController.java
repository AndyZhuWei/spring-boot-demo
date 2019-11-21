package cn.andy.springBoot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhuwei
 * @Date:2019/5/9 10:40
 * @Description:
 */
@RestController
public class HelloController {


    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
}
