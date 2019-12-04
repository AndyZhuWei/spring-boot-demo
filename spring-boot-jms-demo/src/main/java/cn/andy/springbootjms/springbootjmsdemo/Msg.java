package cn.andy.springbootjms.springbootjmsdemo;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @Author: zhuwei
 * @Date:2019/11/28 17:24
 * @Description:
 */
public class Msg implements MessageCreator {

    @Override
     public Message createMessage(Session session) throws JMSException {
        return session.createTextMessage("测试消息");
    }
}
