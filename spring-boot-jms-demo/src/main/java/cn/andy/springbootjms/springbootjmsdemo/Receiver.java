package cn.andy.springbootjms.springbootjmsdemo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author: zhuwei
 * @Date:2019/11/28 22:31
 * @Description:
 */
@Component
public class Receiver {

    //@JmsListener是Spring 4.1为我们提供的一个新特性，用来简化JMS开发。我们
    //只需在这个注解的属性destination指定要监听的目的地，即可接收该目的地发送的消息
    //此例监听my-destination目的地发送的消息
    @JmsListener(destination="my-destination")
    public void receiveMessage(String message) {
        System.out.println("接收到：<"+message+">");
    }


























}
