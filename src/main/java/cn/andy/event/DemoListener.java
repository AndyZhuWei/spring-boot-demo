package cn.andy.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 14:25
 * @Description:
 */
//实现ApplicationListener接口，并指定监听的事件类型
@Component
public class DemoListener implements ApplicationListener<DemoEvent> {

    //使用onApplicationEvent方法对消息进行接受处理
    @Override
    public void onApplicationEvent(DemoEvent demoEvent) {
        String msg = demoEvent.getMsg();
        System.out.println("我（bean-demoListener）接收到了bean-demoPublisher发布的消息："+msg);
    }
}
