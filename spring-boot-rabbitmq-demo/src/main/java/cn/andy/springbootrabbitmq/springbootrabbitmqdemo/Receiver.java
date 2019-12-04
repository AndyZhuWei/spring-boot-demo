package cn.andy.springbootrabbitmq.springbootrabbitmqdemo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: zhuwei
 * @Date:2019/11/29 10:21
 * @Description: 使用@RabbitListener来监听RabbitMQ的目的地发送的消息，通过queue属性指定要监听的目的地
 */
@Component
public class Receiver {


    @RabbitListener(queues = "my-queue")
    public void receiveMessage(String message) {
        System.out.println("Received<" + message + ">");
    }


}
