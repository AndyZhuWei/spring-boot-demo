## 8.6 非关系型数据库NoSQL
NoSQL是对于不使用关系作为数据管理的数据库系统的统称。NoSQL的主要特点是不使用SQL语言作为查询语言，数据存储也不是固定的表、字段。
NoSQL数据库主要有文档存储型(MongoDB)、图形关系存储型(Neo4j)和键值对存储型(Redis)
### 8.6.1 MongoDB
MongoDB是一个基于文档(Document)的存储的数据库，使用面向对象的思想，每一条数据记录都是文档的对象。
1.Spring的支持
Spring对MongoDB的支持主要是通过Spring Data MongoDB来实现的，Spring Data MongoDB为我们提供了如下功能：
（1）Object/Document映射注解支持
JPA提供了一套Object/Relation映射的注解(@Entity、@Id),而Spring Data MongoDB也提供了如下的注解
注解                     描述
@Document               映射领域对象与MongoDB的一个文档
@Id                     映射当前属性是ID
@DbRef                  当前属性将参考其他的文档
@Field                  为文档的属性定义名称
@Version                将当前属性作为版本
（2）MongoTemplate
像JdbcTemplate一样，Spring Data MongoDB也为我们提供了一个MongoTemplate,MongoTemplate为我们提供了数据访问的方法。我们
还需要为MongoClient以及MongoDbFactory来配置数据库连接属性。例如
```java
@Bean
public MongoClient client() throws UnknownHostException {
    MongoClient client = new MongoClient(new ServerAddress("127.0.0.1",27017));
    return client;
}

@Bean
public MongoDbFactory mongoDbFactory() throws Exception {
    String database = new MongoClientURI("mongodb://localhost/test").getDatabase();
    return new SimpleMongoDbFactory(client(),database);
}

@Bean
public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) throws UnknownHostException {
    return new MongoTemplate(mongoDbFactory);
}
```
(3)Repository的支持
类似于Spring Data JPA,Spring Data MongoDB也提供了Repository的支持，使用方式和Spring Data JPA一致，定义如下：
```java
public interface PersonRepository extends MongoRepository<Person,String> {
    
}
```
类似于Spring Data JPA的开启支持方式，MongoDB的Repository的支持开启需在配置类上注解@EnableMongoRepositories，例如
```java
@Configuration
@EnableMongoRepositories
public class AppConfig {
    
}
```
2.Spring Boot的支持
Spring Boot对MongoDB的支持，分别位于
org.springframework.boot.autoconfigure.mongo
主要配置数据库连接、MongoTemplate.我们可以使用以"spring.data.mongodb"为前缀的属性来配置MongoDB相关的信息。Spring Boot为
我们提供了一些默认属性。如默认MongoDB的端口为27017、默认服务器为localhost、默认数据库为test。Spring Boot的主要配置如下：
```properties
spring.data.mongodb.host=#数据库主机地址，默认为localhost
spring.data.mongodb.prot=27017#数据库连接端口默认27017
spring.data.mongodb.uri=mongodb://localhost/test#connectio URL
spring.data.mongodb.database=
spring.data.mongodb.authentication-database=
spring.data.mongodb.grid-fs-database=
spring.data.mongodb.username=
spring.data.mongodb.password=
spring.data.mongodb.repositories.enabled=true#repository支持是否开启，默认为已开启
spring.data.mongodb.field-naming-strategy=org.springframework.boot.autoconfigure.data.mongo
```
为我们开启了对Repository的支持，即自动为我们配置了@EnableMongoRepositories.
所以我们在Spring Boot下使用MongoDB只需引入spring-boot-starter-data-mongodb依赖即可，无须任何配置
3.实战
（1）安装MongoDB
Docker安装
运行Docker容器 docker run -d -p 27017:27017 mongo
MongoDB数据库管理软件可使用Robomongo,下载地址是http://www.robomongo.org
（2）搭建Spring Boot项目
搭建Spring Boot项目，依赖为MongoDB(spring-boot-starter-mongodb)和Web(spring-boot-starter-web)
因为Spring Boot的默认数据库连接满足我们当前测试的要求，所以将不在为application.properties配置连接信息
（3）领域模型
本例的领域模型是人(Person)，包含他工作过的地点（Location）.这个虽然和关系型数据库的一对多类似，但还是不一样的，Location的数据
只属于某个人
（4）数据访问：
PersonRepository
（5）控制器
DataController
（6）运行
测试保存数据
访问http://localhost:8080/save测试保存
我们可以在Robomongo中查看保存后的数据
测试方法名查询
访问http://localhost:8080/q1?name=wyf
测试@Query查询
访问http://localhost:8080/q2?age=32













