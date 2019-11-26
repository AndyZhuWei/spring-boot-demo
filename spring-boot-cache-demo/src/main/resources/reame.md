## 8.5数据缓存Cache
### 8.5.1 Spring缓存支持
Spring定义了org.springframework.cache.CacheManager和org.springframework.cache.Cache接口用来统一不同缓存的技术。其中，CacheManager
是Spring提供的各种缓存技术抽象接口，Cache接口包含缓存的各种操作(增加、删除、获得缓存，我们一般不会直接和此接口打交道)。
1.Spring支持的CacheManager
针对不同的缓存技术，需要实现不同的CacheManager,Spring定义了如下CacheManager实现
CacheManager                     描述
SimpleCacheManager               使用简单的Collection来存储缓存，主要用来测试用途
ConcurrentMapCacheManager        使用ConcurrentMap来存储缓存
NoOpCacheManager                 仅测试用途，不会实际存储缓存
EhCacheCacheManager              使用EhCache作为缓存技术
GuavaCacheManager                使用Google Guava的GuavaCache作为缓存技术
HazelcastCacheManager            使用Hazelcast作为缓存技术
JCacheCacheManager               支持JCache(JSR-107)标准的实现作为缓存技术，如Apache Commons JCS
RedisCacheManager                使用Redis作为缓存技术
在我们使用任意一个实现的CacheManager的时候，需注册实现的CacheManager的Bean
```java
@Bean
public EhCacheCacheManager cacheManager(CacheManager ehCacheCacheManager) {
    return new EhCacheCacheManager(ehCacheCacheManager);
}
```
当然，每种缓存技术都有很多的额外配置，但配置cacheManager是必不可少的。
2.声名式缓存注解
Spring 提供了4个注解来声明缓存规则（又是使用注解式的AOP的一个生动例子）。这4个注解如下：
注解              解释
@Cacheable       在方法执行前Spring先查看缓存中是否有数据，如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放进缓存
@CachePut        无论怎样，都会将方法的返回值放到缓存中。@CachePut的属性与@Cacheable保持一致
@CacheEvict      将一条或多条数据从缓存中删除
@Caching         可以通过@Caching注解组合多个注解策略在一个方法上
以上注解都有value属性，指定的是要使用的缓存的名称，key属性指定的是在缓存中的存储的键
3.开启声名式缓存支持
开启声名式缓存支持十分简单，只需在配置类上使用@EnableCaching注解即可
```java
@Configuration
@EnableCaching
public class AppConfig {
    
}
```
### 8.5.2 Spring Boot的支持
在Spring中使用缓存技术的关键是配置CacheManager,而Spring Boot为我们自动配置了多个CacheManager的实现
Spring Boot的CacheManager的自动配置放置在org.springframework.boot.autoconfigure.cache包中
通过这个包我们可以看出，Spring Boot为我们自动配置了EhCacheCacheConfiguration(使用EhCache)、GenericCacheConfiguration(使用Collection)、
GuavaCacheConfiguration(使用Guava)、HazelcastCacheConfiguration(使用Hazelcast)、InfinispanCacheConfiguration(使用Infinispan)、
JCacheCacheConfiguration(使用JCache)、NoOpCacheConfiguration(不使用存储)、RedisCacheConfiguration(使用Redis)、SimpleCacheConfiguration(
使用ConcurrentMap)。在不做任何额外配置的情况下，默认使用SimpleCacheConfiguration.即使用ConcurrentMapCacheManager。Spring Boot支持以“spring.cache”
为前缀的属性来配置缓存
```properties
spring.cache.type=#可选generic,ehcache,hazelcast,infinispan,jcache,redis,guava,simple,none
spring.cache.cache-names=#程序启动时创建缓存名称
spring.cache.ehcache.config=#ehcache配置文件地址
spring.cache.hazelcast.config=#hazelcast配置文件地址
spring.cache.infinispan.config=#infinispan配置文件地址
spring.cache.jcache.config=#jcache配置文件地址
spring.cache.jcache.provider=#当多个jcache实现在类路径中的时候，指定jcache实现
spring.cache.guava.spec=#guava specs
```
在Spring Boot环境下，使用缓存技术只需在项目中导入相关缓存技术的依赖包，并在配置类使用@EnableCaching开启缓存支持即可
### 8.5.3 实战
本例将以Spring Boot默认的ConcurrentMapCacheManager作为缓存技术，演示@Cacheable、@CachePut、@CacheEvit
最后使用EhCache、Guava来替换缓存技术
1.新建Spring Boot项目
spring-boot-cache-dmeo 依赖为Cache（spring-boot-starter-cache）、JPA（spring-boot-starter-data-jpa）和
Web(spring-boot-starter-web)
添加Oracle JDBC驱动，并在applicaiton.properties属性配置相关属性
2.实体类
Person
3.实体类Repository
PersonRepository
4.业务服务
(1)接口
DemoService
(2)实现类
DemoServiceImpl
5.控制器
CacheController
6.开启缓存支持
@EnableCaching
在Spring Boot中还是要使用@EnableCaching开启缓存支持
7.运行
当我们对数据做了缓存之后，数据的获得将从缓存中得到，而不是从数据库中得到
我们在每次运行测试情况下，都重启了应用程序
（1）测试@Cacheable
第一次访问http://localhost:8080/able?id=1,第一次将调用方法查询数据库，并将数据放到缓存people中
再次访问http://localhost:8080/able?id=1,此时控制台没有再次输出Hibernate的查询语句以及”为id、key为："+p.getId()+"数据做了缓存“
字样，表示没有调用这个方法，页面直接从数据缓存中获得数据
（2）测试@CachePut
访问http://localhost:8080/put?name=cc&age=22&address=成都，
再次访问刚才新增那条数据,加入id为62
http://localhost:8080/able?id=62，控制台无输出，从缓存中直接获得数据
（3）测试@CacheEvit
访问http://localhost:8080/able?id=1,为id为1的数据做缓存，再次访问http://localhost:8080/able?id=1,确认数据已从缓存中获取。
访问http://localhost:8080/evit?id=1，从缓存中删除key为1的缓存数据
再次访问http://localhost:8080/able?id=1,观察控制台重新做了缓存，但是我们已经删除了原始数据，缓存了一个空对象
### 8.5.4 切换缓存技术
切换缓存技术除了移入相关依赖包或者配置以外，使用方式和实战例子保持一致。
1.EhCache
当我们需要使用EhCache作为缓存技术的时候，我们只需在pom.xml中添加EhCache的依赖即可
```xml
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
</dependency>
```
EhCache所需的配置文件ehcache.xml只需妨碍类路径下。Spring Boot会自动扫描，例如：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache>
    <cache name="people" maxElementsInMemory="1000" />
</ehcache>
```
Spring Boot会给我们自动配置EhCacheCacheManager的Bean
2.Guava
当我们需要使用Guava作为缓存技术的时候，我们也只需在pom.xml中增加Guava的依赖即可：
```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>18.0</version>
</dependency>
```
Spring Boot会给我们自动配置GuavaCacheManager这个Bean
3.Redis
使用Redis,只需添加下面的依赖即可：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-redis</artifactId>
</dependency>
```
Spring Boot将会为我们自动配置RedisCacheManager以及RedisTemplate的Bean
























