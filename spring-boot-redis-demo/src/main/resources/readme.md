### 8.6.2 Redis
Redis是一个基于键值对的开源内存数据存储，当然Redis也可以做数据缓存
1.Spring的支持
（1）配置
Spring对Redis的支持也是通过Spring Data Redis来实现的，Spring Data JPA为我们提供了连接相关的ConnectionFactory和数据操作相关
RedisTemplate.在此特别指出，Spring Data Redis只对Redis2.6和2.8版本做过测试
根据Redis的不同的Java客户端，Spring Data Redis提供了如下的ConnectionFactory:
JedisConnectionFactory:使用Jedis作为Redis客户端
JredisConnectionFactory:使用Jredis作为Redis客户端
LettuceConnectionFactory:使用Lettuce作为Redis客户端
SrpConnectionFactory:使用Spullara/redis-protocol作为Redis客户端
配置方式如下：
```java
@Bean
public RedisConnectionFactory redisConnectionFactory {
    return new JedisConnectionFactory();
}
```
RedisTemplate配置方式如下:
```java
@Bean
public RedisTemplate<Object,Object> redisTemplate() throws UnknowHostException {
    RedisTemplate<Object,Object> template = new RedisTemplate<Object,Object>();
    template.setConnectionFactory(redisConnectionFactory());
    return template;
}
```
（2）使用
Spring Data Redis为我们提供了RedisTemplate和StringRedisTemplate两个模板来继续数据操作，其中，StringRedisTemplate只
针对键值都是字符型的数据进行操作。
RedisTemplate和StringRedisTemplate提供的主要数据访问方法如表：
方法                      说明
opsForValue()            操作只有简单属性的数据
opsForList()             操作含有list的数据
opsForSet()              操作含有set的数据
opsForZSet()             操作含有ZSet(有序的set)的数据
opsForHash()             操作含有hash的数据
（3）定义Serializer
当我们的数据存储到Redis的时候，我们的键（key）和值（value）都是通过Spring提供的Serializer序列化到数据库的。RedisTemplate
默认使用的是JdkSerializationRedisSerializer,StringRedisTemplate默认使用的是StringRedisSerializer
Spring Data JPA为我们提供了下面的Serializer:
GenericToStringSerializer、Jackson2JsonRedisSerializer、JacksonJsonRedisSerializer、
JdkSerializationRedisSerializer、OxmSerializer、StringRedisSerializer.
2.Spring Boot的支持
Spring Boot对Redis的支持org.springframework.boot.autoconfigure.redis包
RedisAutoConfiguration为我们默认配置了JedisConnectionFactory、RedisTemplate以及StringRedisTemplate，让我们可以直接
使用Redis作为数据存储
RedisProperties向我们展示了可以使用以”spring.redis“为前缀的属性在application.properties中配置Redis,主要属性如下：
spring.redis.database=0#数据库名称，默认为db0
spring.redis.host=localhost#服务器地址，默认为localhost
spring.redis.password=#数据库密码
spring.redis.port=6379#连接端口号，默认为6379
spring.redis.pool.max-idle=8#连接池设置
spring.redis.pool.min-idle=0
spring.redis.pool.max-active=8
spring.redis.pool.max-wait=-1
spring.redis.sentinel.master=
spring.redis.sentinel.nodes=
spring.redis.timeout=
#实战
（1）安装Redis
Docker安装
docker run -d -p 6379:6379 redis:2.8.21
Redis数据管理可以使用Redis Client
(2)新建Spring Boot项目
spring-boot-redis-demo
搭建Spring Boot项目，依赖为Redis(spring-boot-starter-redis)和Web(spring-boot-starter-web)
（3）领域模型类：
Person
（4）数据访问
PersonDao
（5）配置
Spring Boot为我们自动配置了RedisTemplate，而RedisTemplate使用的是JdkSerializationRedisSerializer,
这个对我们演示Redis Client很不直观，因为JdkSerializationRedisSerializer使用二进制形式存储数据，在此我们将自己配置
RedisTemplate并定义Serializer
SpringBootRedisDemoApplication
(6)控制器
DataController
(7)运行
演示设置字符及对象，访问http://localhost:8080/set
演示获得字符，访问http://localhost:8080/getStr
演示获得对象，访问http://localhost:8080/getPerson

























