## 8.2 Spring Data JPA
### 8.2.1 点睛Spring Data JPA
1.什么是Spring Data JPA
在介绍Spring Data JPA的时候，我们首先认识下Hibernate。Hibernate是数据访问解决技术的绝对霸主，使用O/R映射（Object-Relational Mapping）
技术实现数据访问，O/R映射即将领域模型类和数据库的表进行映射，通过程序操作对象而实现表数据操作的能力，让数据访问操作无须关注数据库相关的技术。
随着Hibernate的盛行，Hibernate主导了EJB3.0的JPA规范，JPA即Java Persistence API.JPA是一个基于O/R映射的标准规范（目前最新版本是JPA2.1）
所谓规范即只定义标准规则（如注解、接口），不提供实现，软件提供商可以按照标准规范来实现，而使用者只需按照规范中定义的方式来使用，而不用和软件提供商的
实现打交道。JPA的主要实现由Hibernate、EclipseLink和OpenJPA等，这也意味着我们只要使用JPA来开发，无论是哪一个开发方式都是一样的。
Spring Data JPA是Spring Data的一个子项目，它通过提供基于JPA的Repository极大地减少了JPA作为数据访问方案的代码量
2.定义数据访问层
使用Spring Data JPA建立数据访问层十分简单，只需定义一个继承JpaRepository的接口即可，定义如下：
```java
public interface PersonRepository extends JpaRepository<Person,Long> {
    //定义数据访问操作的方法
}
```
继承JpaRepository接口意味着我们默认已经有了下面的数据访问操作方法：
```java
@NoRepositoryBean
public interface JpaRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    List<T> findAll();

    List<T> findAll(Sort var1);

    List<T> findAll(Iterable<ID> var1);

    <S extends T> List<S> save(Iterable<S> var1);

    void flush();

    <S extends T> S saveAndFlush(S var1);

    void deleteInBatch(Iterable<T> var1);

    void deleteAllInBatch();

    T getOne(ID var1);

    <S extends T> List<S> findAll(Example<S> var1);

    <S extends T> List<S> findAll(Example<S> var1, Sort var2);
}
```
3.配置使用Spring Data JPA
在Spring环境中，使用Spring Dat API可通过@EnableJpaRepositories注解来开启Spring Data JPA的支持，@EnableJpaRepositories接收的value
参数用来扫描数据访问层所在包下的数据访问的接口定义。
```java
@Configuration
@EnableJpaRepositories("com.wisely.repos")
public class JpaConfiguration {
    @Bean
    public EntityManagerFactory entityManagerFactory(){
        //...
    }
    //还需配置DataSource\PlatformTransactionManager等相关必须bean
    
}
```
4.定义查询方法
在讲解查询方法前，假设我们有一张数据表叫PERSON,有ID(Number)、Name（Varchar2）、Age（Number）、ADDRESS（Varchar2）几个字段；
对应的实体类叫Person，分别有id（Long）、name（String）、age（Integer）、address（String）。下面我们就以这个简单的实体查询作为演示
（1）根据属性名查询
Spring Data JPA支持通过定义在Repository接口中的方法名来定义查询，而方法名是根据实体类的属性名来确定的。
1）常规查询。根据属性名来定义查询方法，
```java
public interface PersonRepository extends JpaRepository<Person,Long>{
    /**
    * 
    * 通过名字相等查询，参数为name
    * 相当于JPQL:select p from Person p where p.name=?1
    * 
    */
    List<Person> findByName(String name);
    /**
    * 
    * 通过名字like查询，参数为name
    * 相当于JPQL:select p from Person p where p.name like ?1
    * 
    */
    List<Person> findByNameLike(String name);
    /**
    * 
    * 通过名字和地址查询，参数为name和address
    * 相当于JPQL:select p from Person p where p.name=?1 and p.address=?2
    * 
    */
    List<Person> findByNameAndAddress(String name,String address);
}
```
从代码可以看出，这里使用了findBy、Like、And这样的关键字。其中findBy可以用find、read、readBy、query、queryBy、get、getBy来替代。
而Like和and这类查询关键字如下：
And                    findByLastnameAndFirstname                  where x.lastname=?1 and x.firstname=?2
Or                     findByLastnameOrFirstname                   where x.lastname=?1 or x.firstname=?2
Is,Equals              findByFirstname,FindByFirstnameIs,          where x.firstname = 1?
                       findByFirstnameEquals
Between                findByStartDateBetween                      where s.startDate between 1? and ?2
LessThan               findByAgeLessThan                           where x.age<?1
LessThanEqual          findByAgeLessThanEqual                      where x.age <=?1
GreaterThan            findByAgeGreaterThan                        where x.age > ?1
GreaterThanEqual       findByAgeGreaterThanEqual                   where x.age >= ?1
After                  findByStartDateAfter                        where x.startDate > ?1
Before                 findByStartDateBefore                       where x.startDate < ?1
IsNull                 findByAgeIsNull                             where x.age is null
IsNotNull,NotNull      findByAge(Is)NotNull                        where x.age not null
Like                   findByFirstnameLike                         where x.firstname like ?1
NotLike                findByFirstnameNotLike                      where x.firstname not like ?1
StartingWith           findByFirstnameStartingWith                 where x.firstname like ?1(参数前面加%)
EndingWith             findByFirstnameEndingWith                   where x.firstname like ?1(参数后面加%)
Containing             findByFirstnameContaining                   where x.firstname like ?1(参数两边加%)
OrderBy                findByAgeOrderLastnameDesc                  where x.age=?1 order by x.lastname desc
Not                    findByLastnameNot                           where x.lastname <> ?1
In                     findByAgeIn(Collection<Age> ages)           where x.age in ?1
NotIn                  findByAgeNotIn(Collection<Aga> age)         where x.age not in ?1
True                   findByActiveTrue()                         where x.active=true
Flase                  findByActiveFalse()                        where x.active=false
IgnoreCase             findByFirstnameIgnoreCase                  where UPPER(x.firstname)=UPPER(?1)

2)限制结果数量。结果数量是用top和first关键字来实现的，例如：
```java
public interface PersonRepository extends JpaRepository<Person,Long>{
    /**
    * 
    * 获得符合查询条件的前10条数据
    */
    List<Person> findFirst10ByName(String name);
    /**
    * 
    * 获得符合查询条件的前30条数据
    */
    List<Person> findTop30ByName(String name);
}
```
(2)使用JPA的NamedQuery查询
Spring Data JPA支持用JPA的NameQuery来定义查询方法，即一个名称映射一个查询语句。定义如下：
```java
@Entiry
@NamedQuery(name="Person.findByName",
      query="select p from Person p where p.name=?1")
public class Person {
    
}
```
使用如下语句：
```java
public interface PersonRepository extends JpaRepository<Person,Long> {
    /**
    *
    * 这时我们使用的是NameQuery里定义的查询语句，而不是根据方法名称查询
    * 
    */
    List<Person> findByName(String name);
}
```
(3)使用@Query查询
1）使用参数索引。Spring Data JPA还支持用@Query注解在接口的方法上实现查询，例如：
```java
public interface PersonRepository extends JpaRepository<Person,Long> {
    @Query("select p from Person p where p.address=?1")
    List<Person> findByAddress(String address);
}
```
2)使用命名参数。上面的例子是使用参数索引号来查询的，在Spring Data JPA里还支持在语句里用名称来匹配查询参数，例如：
```java
public interface PersonRepository extends JpaRepository<Person,Long> {
    @Query("select p from Person p where p.address=:address")
    List<Person> findByAddress(@Param("address") String address);
}
```
3)更新查询。Spring Data JPA支持@modifying和@Query注解组合来事件更新查询，例如：
```java
public interface PersonRepository extends JpaRepository<Person,Long> {
    @Modifying
    @Transactional
    @Query("update Person p set p.name=?1")
    int setName(String name);
}
```
其中返回值int表示更新语句影响的行数
（4）Specification
JPA提供了基于准则查询的方式，即Criteria查询。而Spring Data JPA提供了一个Specification(规范)接口让我们可以更方便地构造准则查询，Specification
接口定义了一个toPredicate方法用来构造查询条件
1）定义。我们的接口类必须实现JpaSpecificationExecutor接口，代码如下：
```java
public interface PersonRepository extends JpaRepository<Person,Long>,JpaSpecificationExecutor<Person> {
    
}
```
然后需要定义Criterial查询，代码如下：
```java
package com.wisely.specs;
public class CustomerSpecs {
    public static Specification<Person> personFromHefei() {
        return new Specification<Person>(){
            @Override
            public Predicate toPredicate(Root<Person> root,CriteriaQuery<?> query,CiteriaBuilder cb) {
                return cb.equal(root.get("address"),"合肥");
            }
        };
    }
}
```
我们使用Root来获得需要查询的属性，通过CriteriaBuilder构造查询条件，本例的含义是查询所有来自合肥的人
注意：CriteriaBuilder、CriteriaQuery、Predicate、Root都是来自己JPA的接口
CriterialBuilder包含的条件构造有：exists、and、or、not、conjnction、disjunction、isTrue、isFalse、isNull、isNotNull、equal、notEqual
、greaterThan、greaterThanOrEqualTo、lessThan、lessThanOrEqualTo、between等。
2）使用，静态导入
import static com.wisely.specs.CustomerSepcs.*;
注入personRepository的Bean后：
List<Person> people = personRepository.findAll(personFromHefie());
(5)排序与分页
Spring Data JPA充分考虑了在实际开发中所必需的排序和分页的场景，为我们提供了Sort类以及Page接口和pageable接口
1）定义：
```java
public interface PersonRepository extends JpaRepository<Person,Long>{
    List<Person> findByName(String name,Sort sort);
    Page<Person> findByName(String name,Pageable pageable);
}
```
2)使用排序
```java
List<People> people = personRepository.findByName("xx",new Sort(Direction.ASC,"age"));
```
3)使用分页
```java
Page<Person> people2 = personRepository.findByName("xx",new PageRequest(0,10));
```
其中Page接口可以获得当前页面的记录、总页数、总记录数、是否有上一页或下一页等
5.自定义Repository的实现
Spring Data提供了和CrudRepository、PagingAndSortingRepository;Spring Data JPA也提供了JpaRepository。如果我们想把自己常用的数据库操作
封装起来，像JpaRepository一样提供给我们领域类的Repository接口使用，应该怎么操作呢？
（1）定义自定义Repository接口
```java
@NoRepositoryBean //1@NoRepositoryBean指明当前这个接口不是我们领域类的接口（如PersonRepository）
//2.我们自定义的Repository实现PagingAndSortingRepository接口，具备分页和排序的能力
public interface CustomRepository<T,ID extends Serializable> extends PagingAndSortingRepository<T,ID> {
    public void doSomething(ID id);//3要定义的数据操作方法在接口中的定义
}
```
（2）定义接口实现：
 ```java
 //1首先要实现CustomRepository接口，继承SimpleJpaRepository类让我们可以使用其提供的方法(如findAll)
public class CustomRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T,ID> 
              implements CustomRepository<T,ID> {
    //2让数据操作方法中可以使用entityManager
    private final EntityManager entityManager;
    
    //3CustomRepositoryImpl的构造函数，需当前处理的领域类型和entityManager作为构造参数，在这里也给我们的entityManager赋值了
    public CustomRepositoryImpl<Class<T> domainClass,EntityManager entityManager> {
        super(domainClass,entityManager);
        this.entityManager = entityManager;
    }
    
    @Override
    public void doSomething(ID id) {
        //4在此处定义数据访问操作，如调用findAll方法并构造一些查询条件
    }
}
```
（3）自定义RepositoryFactoryBean。自定义JpaRepositoryFactoryBean替代默认RepositoryFactoryBean，我们会获得一个RepositoryFactory，
RepositoryFactory将会注册我们自定义的Repository的实现
```java
//1自定义RepositoryFactoryBean，继承JpaRepositoryFactoryBean
public class CustomRepositoryFactoryBean<T extends JpaRepository<S,ID>,S,ID extends Seribalizable> extends JpaRepositoryFactoryBean<T,S,ID> {
   //2重写createRepositoryFactory方法，用当前的CustomRepositoryFactory创建实例。
    @Override
   protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
       return new CustomRepositoryFactory(entityManager);
   } 
   
   //3创建CustomRepositoryFactory,并继承JpaRepositoryFactory
   private static class CustomRepositoryFactory extends JpaRepositoryFactory {
        public CustomRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }
        
        //4重写getTargetRepository方法，获得当前自定义的Repository实现
        @Override
        @SuppressWarnings({"unchecked"})
        protected<T,ID extends Serializable> SimpleJpaRepository<?,?> getTargetRepository(
                RepositoryInfomation information,EntityManager entityManager) {
            return new CustomRepositoryImpl<T,ID>((Class<T>)information.getDomainType(),entityManager);
        }
        
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            //5重写getRepositoryBaseClass,获得当前自定义的Repository实现的类型
            return CustomRepositoryImpl.class;
        }
   }
}
```
（4）开启自定义支持使用@EnableJpaRepositories的repositoryFactoryBeanClass来指定FactoryBean即可。
```java
@EnableJpaRepositories(repositoryFactoryBeanClass=CustomRepositoryFactoryBean.class)
```

### 8.2.2Spring Boot的支持
1.JDBC的自动配置
spring-boot-starter-data-jpa依赖于spring-boot-starter-jdbc，而Spring Boot对JDBC做了一些自动配置。源码放置在
org.springframework.boot.autoconfigure.jdbc
从源码分析可以看出，我们通过”spring.datasource“为前缀的属性自动配置dataSource:
Spring Boot自动开启了注解事务的支持(@EnableTransactionManagement);还配置了一个jdbcTemplate
Spring Boot还提供了一个初始化数据的功能：放置在类路径下的schema.sql文件会自动用来初始化表结构：放置在类路径下的data.sql文件会自动用来填充表数据

2.对JPA的自动配置
Spring Boot对JPA的自动配置放置在org.springframework.boot.autoconfigure.orm.jpa下
从HibernateJpaAutoConfiguration可以看出，Spring Boot默认JPA的实现者是Hibernate：
HibernateJpaAutoConfiguration依赖于DataSourceAutoCofiguration
从JpaProperties的源码可以看出，配置JPA可以使用spring.jpa为前缀的属性在application.properties中配置
从JpaBaseConfiguration的源码中可以看出，Spring Boot为我们配置了transactionMaager、jpaVendorAdapter、entityManagerFactory等Bean。
JpaBaseConfiguration还有一个getPackagesToScan方法，可以自动扫描注解有@Entity的实体类
在Web项目中我们经常会遇到在控制器或页面访问数据的时候出现会话连接已关闭的错误，这时候我们会配置一个Open EntityManager(Session) In View这个
过滤器。令人惊喜的是，Spring Boot为我们自动配置了OpenEntityManagerInViewInterceptor这个Bean，并注册到Spring MVC的拦截器中
3.对Spring Data JPA的自动配置
而Spring Boot对Spring Data JPA的自动配置放置在org.springframework.boot.autoconfigure.data.jpa下
从JpaRepositoriesAutoConfiguration和JpaRepositiesAutoConfigureRegistrar源码可以看出，JpaRepositoriesAutoConfiguration是依赖于
HibernateJpaAutoConfiguration配置的，且Spring Boot自动开启了对Spring Data JPA的支持，即我们无须在配置类显示声明@EnableJpaRepositories
4.Spring Boot下的Spring Data JPA
通过上面的分析可知，我们在Spring Boot下使用Spring Data JPA，在项目的Maven依赖里添加spring-boot-starter-data-jpa，然后只需定义DataSource\
实体类和数据访问层，并在需要使用数据访问的地方注入数据访问层的Bean即可，无须任何额外配置。
### 8.2.3实战
1.安装Oracle XE
Oracle XE是Oracle公司提供的免费开发测试用途的数据库。数据大小限制为4G
我们通过docker安装
运行命令
docker run -d -p 9090:8080 -p 1521:1521 wnameless/oracle-xe-11g
将容器中的oracle XE管理界面的8080端口映射为本机的9090端口，将oracle XE 1521端口映射为本机的1521端口
本容器提供如下的安装信息：
hostname:localhost
端口：1521
SID:XE
username:system/sys
password:oracle
管理界面访问：
url:http://localhost:9000/apex
workspace:internal
username:admin
password:oracle



2.创建一个用户，作为我们程序使用的数据库账号，账号为boot,密码QAZ345fd$
create user boot identified by QAZ345fd$;
授权
grant connect,resource,unlimited tablespace to boot;
alter user boot default tablespace users;
alter user boot temporary tablespace temp;


3.新建Spring Boot项目 spring-data-jpa-demo
安装oracle的驱动
mvn install:install-file -DgroupId=com.oracle "-DartifactId=ojdbc6" 
"-Dversion=11.2.0.2.0" "-Dpackaging=jar" "-Dfile=E:\ojdbc6.jar"
然后在pom.xml加入下面坐标引入ojdbc6
<dependency>
 <groupId>com.oracle</groupId>
 <artifactId>ojdbc6</artifactId>
 <version>11.2.0.2.0</version>
</dependency>
添加google guava依赖，它包含大量java常用的工具类
<dependency>
 <groupId>com.google.guava</groupId>
 <artifactId>guava</artifactId>
 <version>18.0</version>
</dependency>
新建一个data.sql文件放置在src/main/resources下，内容为向表格增加一些数据，数据插入完成后请删除或对此文件改名。
```sql
insert into person(id,name,age,address) values(hibernate_sequence.nextval,'汪云飞',32,'合肥');
insert into person(id,name,age,address) values(hibernate_sequence.nextval,'xx',31,'北京');
insert into person(id,name,age,address) values(hibernate_sequence.nextval,'yy',30,'上海');
insert into person(id,name,age,address) values(hibernate_sequence.nextval,'zz',29,'南京');
insert into person(id,name,age,address) values(hibernate_sequence.nextval,'aa',28,'武汉');
insert into person(id,name,age,address) values(hibernate_sequence.nextval,'bb',27,'合肥');
```
4.配置基本属性
在application.properties里配置数据源和jpa相关属性
```properties
#配置数据源，前缀为spring.datasource
spring.datasource.driverClassName=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@192.168.80.100:1521:xe
spring.datasource.username=boot
spring.datasource.password=QAZ345fd$

#以下配置jpa，前缀为spring.jpa
#1hibernate提供了根据实体类自动维护数据库表结构的功能,可通过spring.jps.hibernate.ddl-autol来配置，有以下可选值：
#create:启动时删除上一次生成的表，并根据实体类生成表，表中数据会被清空
#create-drop:启动时根据实体类生成表，sessionFactory关闭时表会被删除
#update:启动时会根据实体类生成表，当实体类属性变动的时候，表结构也会更新，在初期开发阶段使用此选项
#validate:启动时验证实体类和数据表是否一致，在我们数据结构稳定时采用此选项
#none：不采取任何措施
spring.jpa.hibernate.ddl-auto=update
#2spring.jpa.show-sql用来设置hibernate操作的时候在控制台显示其真实的sql语句。
spring.jpa.show-sql=true
#3让控制器输出的json字符串格式更美观
spring.jackson.serialization.indent_output=true
```
5.定义映射实体类
Hibernate支持自动将实体类映射为数据表格
```java
package cn.andy.springdatajpa.springdatajpademo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * @Author: zhuwei
 * @Date:2019/11/19 14:16
 * @Description:
 */
@Entity//1 @Entity注解指明这是一个和数据库表映射的实体类
@NamedQuery(name="Person.withNameAndAddressNamedQuery",
query="select p from Person p where p.name=?1 and address=?2")
public class Person {
    @Id //2@Id注解指明这个属性映射为数据库的主键
    //3 @GeneratedValue注解默认使用主键生成方式为自增，hibernate会为我们自动生成
    //一个名为HIBERNATE_SEQUENCE的序列
    @GeneratedValue
    private Long id;

    private String name;

    private Integer age;

    private String address;

    public Person(){
        super();
    }

    public Person(Long id,String name, Integer age, String address) {
        super();
        this.id=id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
```

在此例中使用的注解也许和你平时经常使用的注解实体类不大一样，比如没有使用@Table(实体类映射表名)、@Column(属性映射字段名)注解。
这是因为我们是采用正向工程通过实体类生成表结构，而不是通过逆向工程从表结构生成数据库。

@Column是用来映射属性名和字段名的，不注解的时候hibernate会自动根据属性名生成数据表的字段名。如属性名name映射成字段名NAME，
多字母属性如testName会自动映射为TEST_NAME.表名映射规则也是如此
6.定义数据访问接口
详见PersonRepository.java
7.运行
详见DataController.java中的示例

8.自定义Repository实现
结合Specification和自定义Repository实现定制一个自动模糊查询。即对于任意的实体对象进行查询，对象里有几个值我们就查几个值，当
值为字符串时我们就自动like查询，其余的类型使用使用自动等于查询，没有值我们就查询全部
（1）定义Specification
详见CustomerSpecs
（2）定义接口
详见CustomRepository
（3）定义实现
详见CustomRepositoryImpl
（4）定义repositoryFactoryBean
详见CustomRepositoryFactoryBean
（5）使用:
详见DataController中的auto方法
（6）配置：
//在配置类上配置@EnableJpaRepositories，并指定repositoryFactoryBeanClass
//让我们自定义的Repository实现起效
@EnableJpaRepositories(repositoryFactoryBeanClass =
        CustomRepositoryFactoryBean.class)
如果我们不需要自定义Repository实现，则在Spring Data JPA里无须添加@EnableJpaRepositories注解，因为@SpringBootApplication
包含的@EnableAutoConfiguration注解已经开启了对Spring DATA JPA的支持
（7）运行



















































