### 9.4.6 实战
本章将演示读取https://spring.io/blog.atom的新闻聚合文件，atom是一种xml文件，且格式是固定的。
我们将读取到消息通过分类(Category),将消息转到不同的消息通道，将分类为releases和engineering的消息写入磁盘文件，将分类为news的消息通过邮件发送
1.新建Spring Boot项目
spring-boot-integraiton-demo
依赖Integration(spring-boot-starter-integraiton)和mail(spring-boot-starter-mail)
另外我们还要添加Spring Integration对atom及mail的支持
spring-integration-feed
spring-integration-mail
2.读取流程
SpringBootIntegrationDemoApplication#myFlow
3.releases流程
SpringBootIntegrationDemoApplication#releasesFlow
4.engineering流程
SpringBootIntegrationDemoApplication#engineeringFlow
5.news流程
SpringBootIntegrationDemoApplication#newsFlow
还没有调通
6.运行
查看磁盘文件和邮箱是否有数据




















