### 9.3.5AMQP实战
1.安装RabbitMQ
Docker安装
运行 docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management
其中5672是消息代理的端口，15672是Web管理界面的端口，我们使用的是带管理界面的RabbitMQ，管理员账号和密码为guest/guest
2.新建Spring Boot项目
spring-boot-rabbitmq-demo，依赖为AMQP(spring-boot-starter-amqp)
Spring Boot默认我们的Rabiit主机为localhost、端口号为5672，所以我们无须为Spring Boot的application.properties配置RabbitMQ的连接信息
3.发送信息及目的地定义
SpringBootRabbitmqDemoApplication
4.消息监听
Receiver
5.运行
启动程序，程序会自动向目的地my-queue发送消息，而Receiver类注解了@RabblitListener的方法会自动监听my-queue发送的消息
控制台和管理界面可以看到输出的消息



















