package cn.andy.aop;

import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 10:47
 * @Description: 编写使用注解的被拦截类
 */
@Service
public class DemoAnnotationService {
    @Action(name="注解式拦截的add操作")
    public void add(){
        System.out.println("DemoAnnotationService->add");
    }
}
