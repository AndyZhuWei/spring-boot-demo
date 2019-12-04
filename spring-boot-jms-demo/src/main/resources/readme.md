### 9.3.4JMS实战
1.安装ActiveMQ
Docker安装
运行  docker run -d -p 61616:61616 -p 8161:8161 cloudesire/activemq
其中61616是消息代理的端口，8161是ActiveMQ的管理界面端口
访问http://localhost:8161可打开ActiveMQ的管理界面，管理员账号密码默认为admin/admin
我们还可以内嵌ActiveMQ，只要在项目依赖里加入activemq-broker即可
2.新建Spring Boot项目
spring-boot-jms-demo
添加spring-jms和activemq-client依赖
配置ActiveMQ消息代理的地址，在application.properties里使用:
spring.activemq.broker-url=tcp://localhost:61616
在实际情况下，消息的发布者和接收者一般都是分开的，而这里我们为了演示简单，将消息发送者和接收者放在一个程序里。
3.消息定义
定义JMS发送的消息需实现MessageCreator接口，并重写其createMessage方法：
Msg
4.消息发送及目的地定义
SpringBootJmsDemoApplication
5.消息监听
Receiver
6.运行
启动程序，程序会自动向目的地my-destination发送消息，而Receiver类注解了@JmsLinster的方法会自动监听
my-destination发送的消息。
在控制台和管理界面都能看到相关信息




































