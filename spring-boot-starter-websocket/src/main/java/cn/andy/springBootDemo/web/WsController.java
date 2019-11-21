package cn.andy.springBootDemo.web;

import cn.andy.springBootDemo.domain.WiselyMessage;
import cn.andy.springBootDemo.domain.WiselyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * @Author: zhuwei
 * @Date:2018/10/26 11:24
 * @Description:
 */
@Controller
public class WsController {

    //通过SimpMessagingTemplate向浏览器发送消息
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //当浏览器向服务端发送请求时，通过@MessageMapping映射/welcome这个地址，类似于@RequestMapping
    @MessageMapping("/welcome")
    //当服务端有消息时，会对订阅了@SendTo中的路径的浏览器发送消息
    @SendTo("/topic/getResponse")
    public WiselyResponse say(WiselyMessage message) throws Exception {
        Thread.sleep(3000);
        return new WiselyResponse("Welcome, "+message.getName()+"!");
    }

    //在Spring MVC中，可以直接在参数中获得principal,principal中包含当前用户的信息。
    @MessageMapping("/chat")
    public void handleChat(Principal principal,String msg) {
        //这里是一段硬编码，如果发送人是wyf,则发送给wisely,如果发送人是wisely，则
        //发送给wyf
        if(principal.getName().equals("wyf")) {
            //messagingTemplate.convertAndSendToUser向用户发送消息，第一个参数是接收消息
            //的用户，第二个是浏览器订阅的地址，第三个是消息本身
            messagingTemplate.convertAndSendToUser("wisely",
                    "/queue/notifications",principal.getName()+"-send:"
            +msg);
        } else {
            messagingTemplate.convertAndSendToUser("wyf",
                    "/queue/notifications",principal.getName()+"-send:"
                            +msg);
        }
    }


























}
