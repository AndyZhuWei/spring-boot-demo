## 8.3SPring Data REST
### 8.3.1 点睛Spring Data REST
(1) 什么是Spring Data REST
Spring Data REST是基于Spring Data的repository之上，可以将repository自动输出为REST资源。目前Spring DATA REST 支持将Spring DATA JPA、
Spring Data MongoDB、Spring Data Neo4j、Spring Data GemFire以及Spring Data Cassandra的repository自动转换成REST服务。
（2）Spring MVC中配置使用Spring Data REST
Spring Data REST的配置是定义在RepositoryRestMvcConfiguration（org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration）
配置类中已经配置好了，我们可以通过继承此类或者直接在自己的配置类上@Import此配置类。
1）继承方式演示
```java
@Configuration
public class MyRepositoryRestMvcConfiguration extends RepositoryRestMvcAutoConfiguration {
    @Override
    public RepositoryRestConfiguration config() {
        return super.config();
    }
    //其他可重写以config开头的方法
}
```
2）导入方式演示：
```java
@Configuration
@Import(RepositoryRestMvcAutoConfiguration.class)
public class AppConfig{
    
}
```
因在Spring MVC中使用Spring Data REST和在Spring Boot中使用方式一样的，因此我们将在实战环节讲解Spring Data REST.
### 8.3.2 Spring Boot的支持
Spring Boot对Spring Data REST的自动配置放置在Rest中，如
RepositoryRestMvcAutoConfiguraiton和SpringBootRepositoryRestMvcConfiguration
通过SpringBootRepositoryRestMvcConfiguration类的源码我们可以得出，Spring Boot已经为我们自动配置了RepositoryRestConfiguration,所以
在Spring Boot中使用Spring Data REST只需引入spring-boot-starter-data-rest,无须任何配置即可使用
Spring Boot通过在application.properties中配置以“spring.data.rest”为前缀的属性来配置RepositoryRestConfiguration
### 8.3.3 实战
（1）新建Spring Boot项目
依赖JPA(spring-boot-starter-data-jpa)和Rest Repositories(spring-boot-starter-data-rest)
添加Oracle JDBC驱动，并在application.properties配置相关属性
（2）实体类
Person
（3）实体类Repository
PersonRepository
（4）安装Chrome插件Postman REST Client
Postman是一个支持REST的客户端，我们可以用它来测试我们的REST资源。
5REST服务测试
在这里我们使用Postman测试RSET资源服务
（1）jQuery
在实际开发中，在jQuery我们使用$.ajax方法的type属性来修改提交方法:
```js
$.ajax({
    type:"GET",
    dataType:"json",
    url:"http://localhost:8080/persons",
    success:function(data) {
        alert(data);
    }
});
```
(2)AngularJS
在实际开发中，可以使用$http对象来操作：
$http.get(url)

$http.post(url,data)

$http.put(url,data)

$http.delete(url)

(3)列表
在实际开发中，在Postman中使用GET方式访问http://localhost:8080/persons,获得列表数据
(4)获取单一对象
在Postman中使用GET访问http://localhost:8080/persons/1,获得id为1的对象
(5)查询
在上面的自定义实体类Repository中定义了findByNameStartsWith方法，若想此方法也暴露为REST资源，需做如下修改：
```java
public interface PersonRepository extends JpaRepository<Person,Long> {
    @RestResource(path="nameStartsWith",rel="nameStartsWith")
    Person findByNameStartsWith(@Param("name")String name);
}
```
此处StartingWith也是可以的
此时在Postman中使用GET访问http://localhost:8080/persons/search/nameStartsWith?name=汪,可实现查询操作
（6）分页
在Postman中使用GET访问http://localhost:8080/persons/?page=1&size=2,page=1即第二页，size=2即每页数量为2，
从返回结果可以看出，我们不仅能获得当前分页的对象，而且还给出了我们上一页、下一页、第一页、最后一页的REST资源路径。
（7）排序
在Postman中使用GET访问localhost:8080/persons/?sort=age,desc，即按照age属性倒序
（8）保存
向http://localhost:8080/persons发起POST请求，将我们要保存的数据放置在请求体中，数据类型设置为JSON.
通过输出可以看出，保存成功后，我们的新数据id为21
（9）更新
现在我们更新新增的id为21的数据，用PUT方式访问http://localhost:8080/persons/21,并修改提交的数据。提交的数据
在请求体中 是JSON
(10)删除
删除刚才新增的id为21的数据，使用DELETE方式访问http://localhost:8080/persons/21

6.定制
（1）定制根路径
在上面的实战例子中，我们访问的REST资源的路径是在根目录下的，即http://localhost:8080/persons,如果我们需要定制根路径的话，只需
在Spring Boot的application.properties下增加如下定义即可:
spring.data.rest.base-path=/api
此时REST资源的路径变成了http://localhost:8080/api/persons
(2)定制节点路径
上例实战中，我们的节点路径为http://localhost:8080/persons,这是Spring Data REST的默认规则，就是在实体类之后加"s"来形成路径。我们知道person
的复数是people而不是persons,在类似的情况下要对映射的名称进行修改的话，我们需要在实体类Repository上使用@RepositoryRestResource注解的path
属性进行修改，代码如下：
```java
@RepositoryRestResource(path="people")
public interface PersonRepository extends JpaRepository<Person,Long> {
    @RestResource(path="nameStartsWith",rel="nameStartsWith")
    Person findByNameStartsWith(@Param("name")String name);
}
```
此时我们访问REST服务的地址变为:http://localhost:8080/api/people



































































