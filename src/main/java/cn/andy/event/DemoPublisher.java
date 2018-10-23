package cn.andy.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 14:27
 * @Description: 事件发布类
 */
@Component
public class DemoPublisher {

    //注入ApplicationContext用来发布事件
    @Autowired
    ApplicationContext applicationContext;

    //使用ApplicationContext的publishEvent方法来发布
    public void publish(String msg) {
        applicationContext.publishEvent(new DemoEvent(this,msg));
    }
}
