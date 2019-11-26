## 8.4 声明式事务
### 8.4.1Spring的事务机制
所有的数据访问技术都有事务处理机制，这些技术提供了API用来开启事务、提交事务来完成数据操作，或者在发生错误的时候回滚数据。
而Spring的事务机制是用统一的机制来处理不同数据访问技术的事务处理。Spring的事务机制提供了一个PlatformTransactionManager接口，不同的数据
访问技术的事务使用不同的接口实现。如下所示
=========数据访问技术===========                        ===========实现==============
JDBC                                                  DataSourceTransactionManager
JPA                                                   JPATransactionManager
Hibernate                                             HibernateTransactionManager
JDO                                                   JdoTransactionManager
分布式事务                                              JtaTransactionManager

在程序中定义事务管理器的代码如下：
```java
@Bean
public PlateformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(datasource());
    return transactionManager;
}
```
### 8.4.2 声名式事务
Spring支持声名式事务，即使用注解来选择需要使用事务的方法，它使用@Transactional注解在方法上表明该方法需要事务支持。
这是一个基于AOP的实现操作。被注解的方法在被调用时，Spring开启一个新的事务，当方法无异常运行结束后，Spring会提交这个事务。
```java
@Transactional
public void saveSomething(Long id,String name) {
    //数据库操作
}
```
在此处需要特别注意的是，此@Transactional注解来自org.springframework.transaction.annotation包，而不是javax.transaction.
Spring提供了一个@EnableTransactionManagement注解在配置类上来开启声名式事务的支持。使用了@EnableTransactionManagement后，Spring
容器会自动扫描注解@Transactional的方法和类。@EnableTransactionManagement的使用方式如下：
```java
@Configuration
@EnableTransactionManagement
public class AppConfig {
    
}
```
### 8.4.3 注解事务行为
@Transaction有如下的属性来定制事务行为
=====属性=====                                ====含义=====                                     ======默认值=====
propagation                    Propagation定义了事务的生命周期,主要有以下选项                           REQUIRED
                               REQUIRED:方法A调用时没有事务新建一个事务，当在方法A调用另外
                                         一个方法B的时候，方法B将使用相同的事务；如果方法B
                                         发生异常需要数据回滚的时候，整个事务数据回滚
                               REQUIRES_NEW:对于方法A和B,在方法调用的时候无论是否有事务都开启一个新的事务；
                                        这样如果方法B有异常不会导致方法A的数据回滚
                               NESTED:和REQUIRES_NEW类似，但支持JDBC,不支持JPA或Hibernate
                               SUPPORTS:方法调用时有事务就用事务，没事务就不用事务
                               NOT_SUPPORTED:强制方法不在事务中执行，若有事务，在方法调用到结束阶段事务都会被挂起
                               NEVER:强制方法不在事务中执行，若有事务则抛出异常
                               MANDATORY:强制方法在事务中执行，若无事务则抛出异常

isolation                      Isolation(隔离)决定了事务的完整性，处理在多事务对相同数据下的处理机制，       DEFAULT
                               主要包含下面的隔离级别(当然我们也不可以随意设置，这要看当前数据库是否支持)
                               READ_UNCOMMITTED:对于在A事务里修改了一条记录但没有提交事务，在B事务可以
                                                读取到修改后的记录。可导致脏读、不可重复读以及幻读
                               READ_COMMITTED:只有当在A事务里修改了一条记录且提交事务之后，B事务才可以读取
                                              到提交后的记录；阻止脏读，但可能导致不可重复读和幻读
                               REPEATABLE_READ:不仅能实现READ_COMMITTED的功能，而且还能阻止当A事务读取了
                                               一条记录，B事务将不允许修改这条记录；阻止脏读和不可重复读，但可出现幻读
                               SERIALIZABLE:此级别下事务是顺序执行的，可以避免上述级别的缺陷，但开销较大
                               DEFAULT:使用当前数据库的默认隔离级别，如Oracle、SQL Server是READ_COMMITTED；
                                       Mysql是REPEATABLE_READ

timeout                        timeout指定事务过期时间，默认为当前数据库的事务过期时间                    TIMEOUT_DEFAULT
readOnly                       指定当前事务是否是只读事务                                             false
rollbackFor                    指定哪个或者哪些异常可以引起事务回滚                                    Thowable的子类
noRollbackFor                  指定哪个或哪些异常不可以引起事务回滚                                    Throwable的子类

### 8.4.4 类级别使用@Transactional
@Transactional不仅可以注解在方法上，也可以注解在类上。当注解在类上的时候意味着此类的所有public方法都是开启事务的。如果类级别方法级别
同时使用了@Transactional注解，则使用在类级别的注解会重载方法级别的注解。
### 8.4.5 Spring Data JPA的事务支持
Spring Data JPA对所有的默认方法都开启了事务支持，且查询类事务默认启用readOnly = true属性
这些我们在SimpleJpaRepository的源码中可以看到，下面就来看看缩减的SimpleJpaRepository的源码
```java
@Repository
@Transactional(readOnly=true)
public class SimpleJpaRepository<T,ID extends Serializable> implements JpaRepository<T,ID>,
                  JpaSpecificationExecutor<T> {
    @Transactional
    public void delete(ID id){}
    @Transactional
    public void delete(T entity){}
    @Transactional
    public void delete(Iterable<? extends T> entities){}
    @Transactional
    public void deleteInBatch(Iterable<T> entities){}
    @Transactional
    public void deleteAll(){}
    @Transactional
    public void deleteAllInBatch(){}
    public T findOne(ID id){}
    @Override
    public T getOne(ID id){}
    public boolean exists(ID id){}
    public List<T> findAll(){}
    public List<T> findAll(Iterable<ID> ids){}
    public List<T> findAll(Sort sort){}
    public Page<T> findAll(Pageable pageable){}
    public T findOne(Specification<T> spec){}
    public List<T> findAll(Specification<T> spec){}
    public Page<T> findAll(Specification<T> spec,Pageable pageable){}
    public List<T> findAll(Specification<T> spec,Sort sort){}
    public long count(){}
    public long count(Specification<T>) spec{}
    @Transactional
    public <S extends T> S save(S entity){}
    @Transactional
    public <S extends T> S saveAndFlush(S entity){}
    @Transactional
    public <S extends T> List<S> save(Iterable<S> entities){}
    @Transactional
    public void flush(){}
    
}
```                  
从源码我们可以看出，SimpleJpaRepository在类级别定义了@Transactional(readOnly=true).而在和save、delete相关的操作重写了@Transactional
属性，此时readOnly属性是false,其余查询操作readOnly仍然为false
### 8.4.6Spring Boot的事务支持
1.自动配置的事务管理器
在使用JDBC作为数据访问技术的时候，Spring Boot为我们定义了PlatformTransactionManager的实现DataSourceTransactionManager的Bean;
配置见org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration类中的定义：
```java
@Bean
@ConditionalOnMissingBean
@ConditionalOnBean(DataSource.class)
public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(this.dataSource);
}
```                
在使用JPA作为数据访问技术的时候，Spring Boot为我们定义一个PlatformTransactionManager的实现JpaTransactionManager的Bean；配置见
org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration.class类中的定义
```java
@Bean
@ConditionalOnMissingBean(PlatformTransactionManager.class)
public PlateformTransactionManager transactionManager() {
    return new JpaTransactionManager();
}
```
2.自动开启注解事务的支持
Spring Boot专门用于配置事务的类为：org.springframework.boot.autoconfig.transaction.TransactionAutoConfiguration，此配置类依赖于
JpaBaseConfiguration和DataSourceTransactionManagerAutoConfiguration.
而在DataSourceTransactionManagerAutoCofiguration配置里还开启了对声名式事务的支持
```java
@ConditionalOnMissingBean(AbstractTransactionManagementConfiguration.class)
@Configuration
@EnableTransactionManagement
protected static class TransactionManagementConfiguration {
    
}
```
所以在Spring Boot中，无须显示开启使用@EnableTransactionManagerment注解
### 8.4.7 实战
在实际使用中，使用Spring Boot默认的配置就能满足我们绝大多数需求。在本节的实战里，我们将演示如何使用@Transactional使用异常导致数据回滚和使用异常
让数据不回滚
1.新建Spring Boot项目
spring-boot-transactional-demo
依赖为JPA和Web.添加Oracle JDBC驱动，并在application.properties配置相关属性。
2.实体类
person
3.实体类Repository
PersonRepository
4.业务服务Service
(1)服务接口
DemoService
(2)服务实现
DemoServiceImpl
5.控制器
MyController
6.运行
为了更清楚地理解回滚，我们以debug(调式模式)启动程序。并在DemoServiceImpl的savePersonWithRollBack
方法打上断点
(1)回滚
访问http://localhost:8080/rollback?name=汪云飞&age=32
(2)不回滚
访问http://localhost:8080/norollback?name=汪云飞&age=32
                                     
                               
















           
