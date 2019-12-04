package cn.andy.springbootrabbitmq.springbootrabbitmqdemo;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootRabbitmqDemoApplication implements CommandLineRunner {

    //1可注入Spring Boot为我们自动配置好的RabbitTemplate
    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRabbitmqDemoApplication.class, args);
    }

    //2定义目的地即队列，队列名称为my-queue
    @Bean
    public Queue wiselyQueue() {
        return new Queue("my-queue");
    }

    //3通过RabbitTemplate的convertAndSend方法向队列my-queue发送消息
    @Override
    public void run(String... args) throws Exception {
        rabbitTemplate.convertAndSend("my-queue","来自RabbitMQ的问候");
    }



























}
