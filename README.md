# JavaEE开发的颠覆者 Spring Boot实战学习示例

#第8章 Spring Boot的数据访问
Spring Data项目是Spring用来解决数据访问问题的一揽子解决方案，Spring Data是一个伞形项目，包含了大量关系型数据库及非关系型数据库的
数据访问解决方案。Spring Data是我们可以快速且简单地使用普通的数据访问技术及新的数据访问技术。
Spring Data为我们使用统一的API来对数据存储技术进行数据访问提供了支持。这是Spring通过提供Spring Data Common项目来实现的，它是上述
各种Spring Data项目的依赖。Spring Data Commons让我们在使用关系型或非关系型数据访问技术时都使用基于Spring的统一标准，该标准包含
CRUD(创建、获取、更新、删除)、查询、排序和分页的相关的操作。
此处介绍下Spring Data Commons的一个重要概念：Spring Data Repository抽象。使用Spring Data Repository可以极大地减少数据访问层的代码。
既然是数据访问操作的统一标准，那肯定是定义了各种各样和数据访问相关的接口，Spring Data Repository抽象的根接口是Repository接口：
```java
package org.springframework.data.repository;
import java.io.Serializable;
public interface Repository<T,ID extends Serializable>{
    
}
```
从源码中可以看出，它接受领域类(JPA为实体类)和领域类的id类型作为类型参数。
它的子接口CrudRepository定义了和CRUD操作相关的内容
CurdRepository的子接口PagingAndSortingRepository定义了与分页和排序操作相关的内容
不同的数据访问技术也提供了不同的Repository,如Spring Data JPA有JpaRepository、Spring Data MongoDB有MongoRepository
Spring Data项目还给我们提供了一个激动人心的功能，即可以根据属性名进行计数、删除、查询方法等操作。
```java
public interface PersonRepository extends Repository<Person,Long> {
    //按照年龄技术
    Long countByAge(Integer age);
    //按照名字删除
    Long deleteByName(String name);
    //按照名字查询
    List<Person> findByName(String name);
    //按照名字和地址查询
    List<Person> findByNameAndAddress(String name,String address);
}
```

## 8.1 引入Docker
Docker是一个轻量级容器技术，类似于虚拟机技术(xen、kvm、vmware、virtual)。Dorker是直接运行在当前操作系统（Linus）之上，而不是运行
在虚拟机中，但是也实现了虚拟机技术的资源隔离，性能远远高于虚拟机技术。
Docker支持将软件编译成一个镜像(image),在这个镜像里做好对软件的各种配置，然后发布这个镜像，使用者可以运行这个镜像，运行中的镜像称之为容器（container）,
容器的启动是非常快的，一般都是以秒为单位。这个有点像我们平时安装ghost操作系统？系统安装好后软件都有了虽然完全不是一种东西，但是思路是类似的。
目前主流的软件以及非主流的软件大部分都有人将其封装成Docker镜像，我们只需要下载Docker镜像，然后运行镜像就可以快速获得已配置可运行的软件。
### 8.1.1 Docker安装
因为Docker的运行原理是基于Linux的，所以Docker只能在Linux下运行。我们在开发测试的时候，Docker是可以在Windows以及Mac OS X系统下的，运行的原理
是启动一个VirtualBox虚拟机，在此虚拟机里运行Docker
1.Linux下安装
CentOS 安装命令:
sudo yum update
sudo yum install docker
Ubuntu:
sudo apt-get update
sudo apt-get install docker
2.Windows下安装
安装这个软件Boot2Docker
### 8.1.2 Docker常用命令及参数
1.Docker镜像命令
基于Docker镜像是可以自己编译的。
通常情况下,Docker的镜像都放置在Docker官网的Docker Hub上，地址是http://registry.hub.docker.com
(1)Docker镜像检索
除了可以在http://registry.hub.docker.com网站检索镜像以外，还可以用下面命令检索：
docker search 镜像名
检索Redis,输入：
docker search redis
(2)镜像下载
下载镜像通过下面命令实现：
docker pull 镜像名
下载Redis镜像，运行：
docker pull redis
(3)镜像列表
查看本地镜像列表
docker images
其中REPOSITORY是镜像名
TAG是软件版本
latest为最新版
image id是当前镜像的唯一标识
created 是当前镜像创建时间
virtual size是当前镜像的大小
（4）镜像删除
删除指定镜像通过下面命令：
docker rmi image-id
删除所有镜像通过下面命令
docker rmi $(docker image -q)
2.Docker容器命令
（1）容器基本操作
最简单的运行镜像为容器的命令如下：
docker run --name container-name -d image-name
运行一个容器只要通过Docker run命令即可实现，其中，--name参数是为容器取得名称；-d表示 detached,意味着执行完这句话命令后控制台将不会被阻碍，
可以继续输入命令操作；最后的image-name 是要使用哪个镜像来运行容器。
我们来运行一个Redis容器
docker run --name test-redis -d redis
Docker会为我们的容器生成唯一的标识
（2）
容器列表
通过下面命令，查询运行中的容器列表
docker ps
其中CONTAINER ID是在启动的时候生成的ID,IMAGE是该容器使用的镜像；COMMAND是容器启动是调用的命令；CREATED是容器创建时间
STATUS是当前容器的状态PORTS是容器系统所使用的端口号，Redis默认使用6379端口；NAME是刚才给容器定义的名称
docker ps -a
(3)停止和启动容器
1）停止容器
docker stop container-name/container-id
我们可以通过容器名称或容器id来停止容器，以停止上面的Redis容器为例：
docker stop test-redis
此时运行中的容器列表为空。查看所有容器列表命令，可看出此时的STATUS为退出
2）启动容器
启动容器通过下面命令
docker start container-name/container-id
再次启动我么刚才停止的容器：
docker start test-redis
3）端口映射
Docker容器中运行的软件所使用的端口，在本机和本机的局域网是不能访问的，所以我们需要将Docker容器中的端口映射到当前主机的端口号上，这样我们在本机
和本机所在的局域网就能够访问该软件了
Docker的端口映射是通过一个-p参数来实现的。我们以刚才的Redis为例，映射容器的6379端口到本机的6378端口，命令如下
docker run -d -p 6378：6379 --name port-redis redis
4）删除容器
删除单个容器，可通过下面的命令
docker rm container-id
删除所有容器，可通过下面的命令
docker rm $(docker ps -a -q)
5)容器日志
查看当前容器日志，可通过下面的命令
docker logs container-name/container-id
我们查看下上面一个容器的日志
docker logs port-redis
6）登录容器
运行中的容器其实是一个功能完备的linux操作系统，所以我们可以像常规的系统一样登录并访问容器
我们可以使用下面命令，登录访问当前容器，登录后我们可以在容器中进行常规的Linux系统操作命令，还可以使用exit命令退出登录
docker exec -it container-id/container-name bash
### 8.1.3 下载本书所需的Docker镜像
docker pull wnameless/oracle-xe-11g
docker pull mongo
docker pull redis:2.8.21
docker pull cloudesire/activemq
docker pull rabbitmq
docker pull rabbitmq:3-management


## 9.3异步消息
   异步消息主要目的是为了系统与系统之间的通信。所谓异步消息即消息发送者无须等待消息接收者的处理及返回，甚至无须关心消息是否发送成功。
   在异步消息中有两个很重要的概念，即消息代理(message broker)和目的地(destination).当消息发送者发送消息后，消息将由消息代理
接管，消息代理保证消息传递到指定的目的地。
   异步消息主要有两种形式的目的地：队列(queue)和主题(topic)。队列用于点对点式(point-to-point)的消息通信；主题用于发布/订阅式(
publish-subscribe)的消息通信。
1.点对点式
   当消息发送者发送消息，消息代理获得消息后将消息放进一个队列(queue)里，当有消息接收者来接收消息的时候，消息将从队列里取出来传递给接收者，
这时候队列里就没有了这条消息。
   点对点式确保的是每一条消息只有唯一的发送者和接收者，但这并不能说明只有一个接收者可以从队列里接收消息。因为队列里有多个消息，点对点式只
保证每一条消息只有唯一的发送者和接收者。
2.发布/订阅式
  和点对点式不同，发布/订阅式是消息发送者发送消息到主题(topic)，而多个消息接收者监听这个主题。此时的消息发送者和接收者分别叫做发布者和订阅者
### 9.3.1 企业级消息代理
  JMS(Java Message Service)即Java消息服务，是基于JVM消息代理的规范。而ActiveMQ、HornetQ是一个JMS消息代理的实现。
  AMQP（Advanced Message Queuing Protocol）也是一个消息代理的规范，但它不仅兼容JMS,还支持跨语言和平台。AMQP的主要实现有RabbitMQ
### 9.3.2 Spring的支持

  Spring对JMS和AMQP的支持分别来自于spring-jms和spring-rabbit
  它们分别需要ConnectionFactory的实现来连接消息代理，并分别提供了JmsTemplate、RabbitTemplate来发送消息
  Spring为JMS、AMQP提供了@JmsListener、@RabbitListener注解在方法上监听消息代理发布的消息。我们需要分别通过@EnableJms、@EnableRabbit开启支持。
### 9.3.3 Spring Boot的支持
  Spring Boot对JMS的自动配置支持位于org.springframework.boot.autoconfigure.jms下，支持JMS的实现有ActiveMQ、HornetQ、Artemis(由HornetQ
捐赠给ActiveMQ的代码块形成的ActiveMQ的子项目)。这里我们以ActiveMQ为例，Spring Boot为我们定义了ActiveMQConnectionFactory的Bean作为连接，
并通过”spring.activemq“为前缀的属性来配置ActiveMQ的连接属性，包含：
spring.activemq.broker-url=tcp://localhost:61616#消息代理的路径
spring.activemq.user=       
spring.activemq.password=       
spring.activemq.in-memory=true       
spring.activemq.pooled=false
  Spring Boot在JmsAutoConfiguration还为我们配置好了JmsTemplate，且为我们开启了注解式消息监听的支持，即自动开启@EnableJms
  Spring Boot对AMQP的自动配置支持位于org.springframework.boot.autoconfigure.amqp下，它为我们配置了连接的ConnectionFactory
和RabbitTemplate,且为我们开启了注解式消息监听，即自动开启@EnableRabbit。RabbitMQ的配置可通过"spring.rabbitmq"来配置RabbitMQ,
主要包含:
spring.rabbitmq.host=localhost#rabbitmq服务地址，默认为localhost
spring.rabbitmq.port=5672#rabbitmq端口，默认为5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=secret
### 9.3.4JMS实战
参见spring-boot-jms-demo
### 9.3.5 AMQP实战
参见spring-boot-rabbitmq-demo


## 9.4系统集成Spring Integration
### 9.4.1Spring Integration快速入门
    Spring Ingegration提供了基于Spring的EIP(Enterprise Integration Patterns，企业集成模式)
的实现。Spring Integration主要解决的问题是不同系统之间交互的问题，通过异步消息驱动来达到系统交互时系统之间的松耦合。本节将基于无XMLp配置的原则
使用Java配置、注解以及Spring Integration Java DSL来使用Spring Integration
    Spring Integration主要由Message、Channel和Message EndPoint组成。
### 9.4.2 Message
    Message是用来在不同部分之间传递的数据。Message由两部分组成:消息体(payload)与消息头(header)。消息体可以是任何数据类型(如XML、JSON、Java
对象);消息头表示的元数据就是解释消息体的内容
```java
public interface Message<T> {
    T getPayload();
    MessageHeaders getHeaders();
}
```
### 9.4.3Channel
在消息系统中，消息发送者发送消息到通道(Channel)，消息接收者从通道(Chanel)接收消息
1.顶级接口
（1）MessageChanel
MessageChannel是Spring Integration消息通道的顶级接口：
```java
public interface MessageChannel {
    public static final long INDEFINITE_TIMEOUT = -1;
    boolean send(Message<?> message);
    boolean send(Message<?> message,long timeout);
}
```
当使用send方法发送消息时，返回值为true，则表示消息发送成功。MessageChannel有两大子接口，分别为PollableChannel(可轮询)和SubscribableChannel(
可订阅)。我们所有的消息通道类都是实现这两个接口。
（2）PollableChannel
PollableChannel具备轮询获得消息的能力，定义如下：
```java
public interface PollableChannel extends MessageChannel {
    Message<?> receive();
    Message<?> receive(long timeout);
}
```
(3)SubscribableChannel
SubscribableChannel发送消息给订阅了MessageHandler的订阅者
```java
public interface SubscribableChannel extends MessageChannel {
    boolean subscribe(MessageHandler handler);
    boolean unsbuscribe(MessageHndler handler);
}
```
2.常用消息通道
（1）PublishSubscribeChannel
PublishSubscribeChannel允许广播消息给所有订阅者，配置方式如下：
```java
@Bean
public PublishSubscribeChannel publishSubscribeChannel() {
    PublishSubscribeChannel channel = new PublishSubscribeChannel();
    return channel;
}
```
当中，当前消息通道的id为publishSubscribeChannel
(2)QueueChannel
QueueChannel允许消息接收者轮询获得信息，用一个队列(queue)接收消息，队列的容量大小可配置，配置方式如下：
```java
@Bean
public QueueChannel queueChannel() {
    QueueChannel channel = new QueueChannel(10);
    return channel;
}
```
其中QueueChannel构造参数10即为队列的容量
（3）PriorityChannel
PriorityChannel可按照优先级将数据存储到对，它依据于消息的消息头priority属性，配置方式如下：
```java
@Bean
public PriorityChannel priorityChannel() {
    PriorityChannel channel = new PriorityChannel(10);
    return channel;
}
```
(4)RendezvousChannel
RendezvousChannel确保每一个接收者都接收到消息后再发送消息，配置方式如下:
```java
@Bean
public RendezvousChannel rendezvousChannel() {
    RendezvousChannel channel = new RendezvousChannel();
    return channel;
}
```
（5）DirectChannel
DirectChannel是Spring Integration默认的消息通道，它允许将消息发送给为一个订阅者，然后阻碍发送直到消息被接收，配置方式如下：
```java
@Bean 
public DirectChannel directChannel() {
    DirectChannel channel = new DirectChannel();
    return channel;
}
```
(6)ExecutorChannel
ExecutorChannel可绑定一个多线程的task executor，配置方式如下：
```java
@Bean 
public ExecutorChannel executorChannel() {
    ExecutorChannel channel = new ExecutorChanenl(executor());
    return channel;
}

@Bean 
public Executor executor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(5);
    taskExecutor.setMaxPoolSize(10);
    taskExecutor.setQueueCapacity(10);
    taskExecutor.initialize();
    return taskExecutor;
}
```
3.通道拦截器
Spring Integration给消息通道提供了通道拦截器（ChannelInterceptor），用来拦截发送和接收消息的操作。
ChannelInterceptor接口定义如下，我们只需实现这个接口即可:
...
我们通过下面的代码给所有的channel增加拦截器:
channel.addInterceptor(someInterceptor);
### 9.4.4 Message EndPoint
消息端点(Message Endpoint)是真正处理消息的(Message)组件，它还可以控制通道的路由。我们可用的消息端点包含如下：
（1）Channel Adapter
通道适配器(Channel Adapter)是一种连接外部系统或传输协议的端点(EndPoint)，可以分为入站(inbound)和出站(outbound)
通道适配器是单向的，入站通道适配器只支持接收消息，出站通道适配器只支持输出消息。
Spring Integration内置了如下的适配器:
RabbitMQ、Feed、File、FTP/SFTP、Gemfire、HTTP、TCP/UDP、JDBC、JPA、JMS、Mail、MongoDB、Redis、RMI、Twitter、
XMPP、WebServices(SOAP、REST)、WebSocket等
Spring Integration extensions项目提供了更多的支持，地址为https://github.com/springprojects/spring-integration-extensions
(2)Gateway
消息网关(Gateway)类似于Adapter，但是提供了双向的请求/返回集成方式，也分为入站(inbound)和出站(outbound)。Spring Integration对相应的
Adapter多都提供了Gateway
(3)Service Activator
Service Activator可调用Spring的Bean来处理消息，并将处理后的结果输出到指定的消息通道
（4）Router
路由(Router)可根据消息体类型(Payload Type Router)、消息头的值(Header Value Router)以及定义好的接收表(Recipient List Router)作为条件，
来决定消息传递的通道。
（5）Filter
过滤器（Filter）类似于路由（Router）,不同的是过滤器不决定消息路由到哪里，而是决定消息是否可以传递给消息通道。
（6）Splitter
拆分器（Splitter）将消息拆分为几个部分单独处理，拆分器处理的返回值是一个集合或者数组
（7）Aggregator
聚合器（Aggregator）与拆分器相反，它接收一个java.util.List作为参数，将多个消息合并为一个消息
（8）Enricher
当我们从外部获得消息后，需要增加额外的消息到已有的消息中，这时就需要使用消息增强器(Enricher)。消息增强器主要有消息体增强器(Payload Enricher)
和消息头增强器(Header Enricher)两种
（9）Transformer
转换器(Transformer)是对获得的消息进行一定的逻辑转换处理（如数据格式转换）。
（10）Bridge
使用连接桥（Bridge）可以简单地将两个消息通道连接起来
### 9.4.5 Spring Integration Java DSL
Spring Integration 提供了一个IntegrationFlow来定义系统继承流程，而通过IntegrationFlows和IntegrationFlowBuilder来实现使用Fluent API
来定义流程。在Fluent API里，分别提供了下面方法来映射Spring Integration的端点(EndPoint).
transform() -> Transformer
filter()   -> Filter
handle()   -> ServiceActivator、Adapter、Gateway
split()  -> Splitter
aggrregate()  ->  Aggregator
route()   -> Router
bridge()   ->  Bridge
一个简单的流程定义如下：
```java
@Bean 
public IntegrationFlow demoFlow() {
    return IntegrationFlows.form("input")//从Channel input获取消息
       .<String,Integer>transform(Integer::parseInt)//将消息转换成整数
       .get();//获得集成流程并注册为Bean
}
```
### 9.4.6 实战
spring-boot-integration-demo
# 第10章 Spring Boot开发部署与测试
## 10.1开发的热部署
### 10.1.1 模板热部署
在Spring Boot里，模板引擎的页面默认是开启缓存的，如果修改了页面的内容，则刷新页面是得不到修改后的页面的，因此我们可以在application.properties
中关闭模板引擎的缓存。例如
Thymeleaf的配置：
spring.thymeleaf.cache=false
FreeMarker的配置:
spring.freemarker.cache=false
Groovy的配置：
spring.groovy.template.cache=false
Velocity的配置：
spring.velocity.cache=false
###10.1.2 Spring Loaded
Spring Loaded可实现修改类文件的热部署。下载Spring Loaded.
然后在启动程序时在vm arguments中输入一下内容：
-javaagent:xxxx\springloaded-1.2.3.RELEASE.jar -noverify
### 10.1.3 JRebel
JRebel是Java开发热部署的最佳工具，其对Spring Boot也提供了极佳的支持。JRebel为收费软件，可试用14天
### 10.1.4spring-boot-devtools
在Spring Boot项目中添加spring-boot-devtools依赖即可实现页面，即代码的热部署
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
</dependency>
## 10.2常规部署
### 10.2.1jar形式
1.打包
若我们在新建Spring Boot项目的时候，选择打包的方式为jar，则我么只需要用：
mvn package
2.运行
java -jar xx.jar
3.注册为Linux的服务
Linux下运行的软件我们通常把它注册为服务，这样我们就可以通过命令开启、关闭以及保持开机启动等功能
若想使用此项功能，我们需要将代码中关于spring-boot-maven-plugin的配置修改为：
<build>
 <plugins>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-maven-plugin</artifactId>
   <configuration>
     <executable>true</executable>
   </configuration>
 </plugins>
</build>
然后使用mvn package打包
主流的linux大多使用init.d或systemd来注册服务，下面以CentOS 6.6演示init.d注册服务：以CentOs 7.1演示systemd注册服务。
用SSH客户端将jar包上传到CentOS的/var/apps下
（1）安装JDK
从Oracle官网下载JDK，注意选择的是:jdk-8u51-linux-x64.rpm。这是红帽系Linux系统专用安装包格式，执行下面命令安装JDK
rpm -ivh jdk-8u51-linux-x64.rpm
 (2)基于Linux的init.d部署
 注册服务，在CentOS6.6的终端执行:
 sudo ln -s /var/apps/ch10-xxx.jar /etc/init.d/ch10
 其中ch10就是我们的服务名
 启动服务
 service ch10 start
 停止服务
 service ch10 stop
 服务状态
 service ch10 status
 开机启动：
 chkconfig ch10 on
 项目日志存放于/var/log/ch10.log下，可用cat或tail等命令查看
 （3）基于Linux的Systemd部署
 在/etc/systemd/system/目录下新建文件ch10.service,填入下面内容:
 [Unit]
 Description=ch10
 After=syslog.target
 
 [Service]
 ExecStart=/usr/bin/java -jar /var/apps/ch10-xxx.jar

 [Install]
 WantedBy=multi-user.target
 注意，在实际使用中修改Description和ExecStart后面的内容
 启动服务
 systemctl start ch10
 或systemctl start ch10.service
 停止服务
 systemctl stop ch10
 或systemctl stop ch10.service
 服务状态:
 systemctl status ch10
 或systemctl status ch10.service
 开机启动
 systemctl enable ch10
 或systemctl enable ch10.service
 项目日志
 journalctl -u ch10
 或journalctl -u ch10.service
 ### 10.2.2 war形式
 新建spring Boot项目时可选择打包方式是war形式
 jar包转换成war包
 修改packaging为war
 增加下面依赖覆盖默认内嵌的tomcat依赖
 ```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```
增加ServletInitializer类
```java
public class ServletInitializer extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicatinBuilder application) {
        return application.sources(XXXApplication.class);
    } 
}
```
## 10.3 云部署——基于Docker的部署
本节我们将在CentOS 7.1上演示用Docker部署Spring Boot程序。前面我们讲述了使用已经编译好的Docker镜像，本节我们将讲述如何编译自己的Docker镜像，
并运行镜像的容器。
主流的云计算(PAAS)平台都支持发布Docker镜像。Docker是使用Dockerfile文件来编译自己的镜像的。
### 10.3.1Dockerfile
Dockerfile主要有如下的指令。
（1）FROM指令
FROM指令指明了当前镜像继承的基镜像。编译当前镜像时会自动下载基镜像。
示例：
FROM ubuntu
（2）MAINTAINER指令
MAINTAINER指令指明了当前镜像的作者
示例：
MAINTAINER wyf
（3）
RUN指令
RUN指令可以在当前镜像上执行Linux命令并形成一个新的层。RUN是编译时（build）的动作。
示例可有如下两种格式，CMD和ENTRYPOINT也是如此：
RUN /bin/bash -c "echo helloworld"
或RUN {"/bin/bash","-c","echo hello"}
(4)CMD指令
CMD指令指明了启动镜像容器时的默认行为，一个Dockerfile里只能有一个CMD指令。CMD指令里设定的命令可以在运行镜像时使用参数覆盖。CMD是运行时(run)的动作。
示例：
CMD echo "this is a test"
可被docker run -d image_name echo "this is not a test"覆盖
(5)EXPOSE指令
EXPOSE指明了镜像运行时的容器必需监听指定的端口。
示例：
EXPOSE 8080
(6)ENV指令
ENV指令可用来设置环境变量
示例：
ENV myName = wyf
或ENV myName wyf
（7）ADD指令
ADD指令是从当前工作目录复制文件到镜像目录中去。
示例：
ADD test.txt /mydir/
(8)ENTRYPOINT指令
ENTRYPOINT指令可以让容器像一个可执行程序一样运行，这样镜像运行时可以像软件一样接收参数执行。ENTRYPOINT是运行时（run）的动作
示例：
ENTRYPOINT ["/bin/echo"]
我们可以向镜像传递参数运行：
docker run -d image_name "this is not a test"
### 10.3.2 安装Docker
yum install docker
启动Docker并保持开机自启:
systemctl start docker
systemctl enable docker
### 10.3.3 项目目录及文件
我们以docker-demo-0.0.1-SNAPSHOT.jar为演示项目。
这个项目很简单，只是在DockerDemoApplication中加入
 @RequestMapping("/")
    public String home() {
        return "Hello Docker!!!";
    }
将打好的jar包上传到centos上，在相同目录下
新建一个Dockerfile文件，内容如下：
FROM java:8

MAINTAINER wyf

ADD docker-demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
解释：
FROM java:8   基镜像为Java,标签(版本)为8
MAINTAINER wyf  作者为wyf
ADD docker-demo-0.0.1-SNAPSHOT.jar app.jar  将我们的docker-demo-0.0.1-SNAPSHOT.jar添加到镜像中，并重命名为app.jar
EXPOSE 8080  运行镜像的容器，监听8080端口
ENTRYPOINT ["java","-jar","/app.jar"]  启动时运行java -jar app.jar
### 10.3.4 编译镜像
在dockerfile所在的目录执行下面的命令，执行编译镜像
docker build -t wisely/ch10docker .
其中wisely/ch10docker为镜像名，我们设置wisely作为前缀，这也是Docker镜像的一种命名习惯。
注意，最后一个“.”，这是用来指明Dockerfile路径的，编译完成后，查看本地镜像 docker images
### 10.3.5运行
通过下面命令运行
docker run -d --name ch10 -p 8080:8080 wisely/ch10docker
查看我们当前的容器状态
docker ps
## 10.4Spring Boot的测试
    Spring Boot的测试和Spring MVC的测试类似。Spring Boot为我们提供了一个@SpringApplicationConfiguration来替代@ContextConfiguration,
用来配置ApplicationContext。
    在Spring Boot中，每次新建项目的时候，都会自动加上spring-boot-starter-test的依赖，这样我们就没有必要测试时再添加额外的jar包。
    Spring Boot还会建一个当前项目的测试类，位于src/test/java的根包下。
    本节我们将直接演示一个简单的测试，测试某一控制器方法是否满足测试用例。
### 10.4.1 新建Spring Boot项目

依赖为JPA、Web、hsqldb（内存数据库）

### 10.4.2 业务代码

# 第11章 应用监控
SpringBoot提供了运行时的应用监控和管理的功能。我们可以通过http、JMS、SSH协议来进行操作。审计、健康及指标信息将会自动得到。
Spring Boot提供了监控和管理端点，如下：
端点名                        描述
actuator                 所有EndPoint的列表，需加入spring HATEOAS支持
autoconfig               当前应用的所有自动配置
beans                    当前应用中所有Bean的信息
configprops              当前应用中所有的配置属性
dump                     显示当前应用线程状态信息
evn                      显示当前应用当前环境信息
health                   显示当前应用健康状况
info                     显示当前应用信息
metrics                  显示当前应用的各项指标信息
shutdown                 关闭当前应用(默认关闭)
trace                    显示追踪信息（默认最新的http请求）
## 11.1 http
我们可以通过http实现对应用的监控和管理，我们只需在pom.xml中增加下面依赖即可：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
既然通过http监控和管理，那么我们的项目中必然需要Web的依赖。本节需新建Spring Boot项目，依赖为：Actuator、Web、HATEOAS

































