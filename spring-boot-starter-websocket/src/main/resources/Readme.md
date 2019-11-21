## 7.6WebSocket
### 7.6.1什么是WebSocket
WebSocket为浏览器和服务器端提供了双工异步通信的功能，即浏览器可以向服务端发送消息，服务端也可以向浏览器
发送消息。WebSocket需浏览器的支持
WebSocket是通过一个socket来实现双工异步通信能力的。但是直接使用WebSocket（或者SockJS:WebSocket协议
的模拟，增加了当浏览器不支持WebSocket的时候的兼容性）协议开发程序显得特别繁琐，我们会使用它的子协议STOMP,
它是一个更高级别的协议，STOMP协议使用一个基于帧(frame)的格式来定义消息，与HTTP的request和response类似（
具有类似于@RequestMapping的@MessageMapping），我们会在后面实战内容中观察STOMP的帧。
### 7.6.2 Spring Boot提供的自动配置
   Spring Boot对内嵌的Tomcat\Jetty9和Undertow使用WebSocket提供了支持。配置源码存于
org.springframework.boot.autoconfigure.websocket下
Spring Boot为WebSocket提供的start pom是spring-boot-starter-websocket
### 7.6.3 实战
1.准备
新增 Spring Boot项目 依赖Thymeleaf和Websocket依赖
2.广播式
广播式即服务端有消息时，会将消息发送给所有连接了当前endpoint的浏览器
（1）配置WebSocket，需要在配置类上使用@EnableWebSocketMessageBroker开启WebSocket支持，
并通过继承AbstractWebSocketMessageBrokerConfigurer类，重写其方法来配置WebSocket
详见WebSocketConfig代码
（2）浏览器发送消息时用WiselyMessage接收
（3）服务端向浏览器发送的此类消息用WiselyResponse
（4）演示控制器
```java
@Controller
public class WsController {

    //当浏览器向服务端发送请求时，通过@MessageMapping映射/welcome这个地址，类似于@RequestMapping
    @MessageMapping("/welcome")
    //当服务端有消息时，会对订阅了@SendTo中的路径的浏览器发送消息
    @SendTo("/topic/getResponse")
    public WiselyResponse say(WiselyMessage message) throws Exception {
        Thread.sleep(3000);
        return new WiselyResponse("Welcome, "+message.getName()+"!");
    }
}
```
（5）添加脚本
将stomp.js（STOMP协议的客户端脚本）、sockjs.min.js(SockJS的客户端脚本)以及jQuery放置在src/main/resources/static下。
（6）演示页面
在src/main/resources/templates下新建ws.html
（7）配置viewController，为ws.html提供便捷的路径映射
详见WebMvcConfig
（8）运行。我们预期的效果是：当一个浏览器发送一个消息到服务端时，其他浏览器也能接收到服务端发送来的这个消息
开启三个浏览器窗口，并访问http://localhost:8080/ws，分别连接服务器。然后在一个浏览器中发送一条消息，其他浏览器接收消息
通过观察在浏览器中观察STOMP的帧，可以看到：
*连接服务端的格式为：
CONNECT
accept-version:1.1,1.0
heart-beat:10000,10000
*连接成功的返回为:
CONNECTED
version:1.1
heart-beat:0,0
*订阅目标(destination)/topic/getResponse:
SUBSCRIBE
id:sub-0
destination:/topic/getResponse
*向目标(destination)/welcome发送消息的格式为：
SEND
destination:/welcome
content-length:14
{\"name\":\"wfy\"}
*从目标(destination)/topic/getResponse接收的格式为:
MESSAGE
destination:/topic/getResponse
content-type:application/json;charset=UTF-8
subscription:sub-0
message-id:zxj4wyan-0
content-length:35
{\"responseMessage\":\"Welcome,wfy!\"}

3.点对点式
广播式有自己的应用场景，但是广播式不能解决我们一个常见的场景，即消息由谁发送、由谁接收的问题
本例中演示一个简单的聊天室程序。例子中只有两个用户，互相发送消息给彼此，因需要用户相关的内容，所以先在这里引入最简单的
Spring Security相关内容
（1）添加Spring Security的starter pom:
```text
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```
(2)Spring Security的简单配置。这里不对Spring Security做过多解释，只解释对本项目有帮助的部分：
详见WebSecurityConfig
(3)配置WebSocket
详见WebSocketConfig
(4)控制器
详见WsController
(5)登录页面
在src/main/resources/templates下新建login.html
(6)聊天页面。
在src/main/resources/templates下新建chat.html
(7)增加页面的viewController:
详见WebMvcConfig
(8)运行。我们预期的效果是：两个用户登录系统，可以互发消息。但是一个浏览器的用户会话session是共享的，
我们可以在谷歌浏览器设置两个独立的用户，从而实现用户会话session隔离
现在分别在两个用户下的浏览器访问:http://localhost:8080/login,并登录
wyf用户向wisely用户发送消息
wisely用户向wyf用户发送消息





























