## 9.2批处理Spring Batch
### 9.2.1Spring Batch快速入门
1.什么是Spring Batch
Spring Batch是用来处理大量数据操作的一个框架，主要用来读取大量数据，然后进行一定处理后输出成指定的形式。
2.Spring Batch主要组成
SpringBatch主要由以下几部分组成
名称                    用途
JobRepository          用来注册Job的容器
JobLauncher            用来启动Job的接口
Job                    我们要实际执行的任务，包含一个或多个Step
Step                   Step步骤包含ItemReader、ItemProcessor和ItemWriter
ItemReader             用来读取数据的接口
ItemProcessor          用来处理数据的接口
ItemWriter             用来输出数据的接口
以上Sprig Batch的主要组成部分只需注册成Spring的Bean即可。若想开启批处理的支持还需在配置类上使用@EnableBatchProcessing
一个示意的Spring Batch的配置如下：
```java
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    
    @Bean
    public JobRepository jobRepository(DataSource dataSource,
         PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType("oracle");
        return jobRepositoryFactoryBean.getObject();
    }
    
    @Bean
    public SimpleJobLauncher jobLauncher(DataSource dataSource,
         PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository(dataSource,transactionManager));
        return jobLauncher;
    }
    
    @Bean
    public Job importJob(JobBuilderFactory jobs,Step s1) {
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }
    
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader<Person> reader,ItemWriter<Person> writer,
         ItemProcessor<Person,Person> processor) {
        return stepBuilderFactory
               .get("step1")
               .<Person,Person>chunk(65000)
               .reader(reader)
               .processor(processor)
               .writer(writer)
               .build(); 
    }
    
    @Bean
    public ItemReader<Person> reader() throws Exception {
        //新建IterReader接口的实现类返回
        return reader;
    }
    
    @Bean
    public IterProcessor<Person,Person> processor() {
        //新建ItemProcessor接口的实现类返回
        return processor;
    }
    
    @Bean
    public ItemWriter<Person> writer(DataSource dataSource) {
        //新建ItemWriter接口的实现类返回
        return writer;
    }
}
```

3.Job监听
若需要监听我们的Job的执行情况，则定义一个类实现JobExecutionListener,并在定义Job的Bean上绑定该监听器
监听器的定义如下：
```java
public class MyJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        //Job开始前
    }
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        //Job完成后
    }
}
```
注册并绑定监听器到Job：
```java
@Bean
public Job importJob(JobBuilderFactory jobs,Step s1) {
    return jobs.get("importJob")
              .incrementer(new RunIdIncrementer())
              .flow(s1)
              .end()
              .listener(myJobListener())
              .builder();
}

@Bean
public MyJobListener myJobListener() {
    return new MyJobListener();
}
```
4.数据读取
Spring Batch为我们提供了大量的ItemReader的实现，用来读取不同的数据来源
5.数据处理及校验
数据处理和校验都要通过ItemProcessor接口实现来完成
(1)数据处理
数据处理只需实现ItemProcessor接口，重写其process方法。方法输入的参数是从ItemReader读取到的数据，返回的数据给ItemWriter.
```java
public class MyItemProcessor implements ItemProcessor<Person,Person> {
    @Override
    public Person process(Person person) {
        String name = person.getName().toUpperCase();
        person.setName(name);
        return person;
    }
}
```
(2)数据校验
我们可以JSR-303(主要实现有hibernate-validator)的注解，来校验ItemReader读取到的数据是否满足要求
我们可以让我们的ItemProcessor实现ValidatingItemProcessor接口：
```java
public class MyItemProcessor extends ValidatingItemProcessor<Person> {
    @Override
    public Person process(Person item) throws ValidationException {
        super.process(item);
        return item;
    }
}
```
定义我们的校验器，实现的Validator接口来自于Spring,我们将使用JSR-303的Validator来校验：
```java
public class MyBeanValidator<T> implements Validator<T>,InitializingBean {
    private javax.validation.Validator validator;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory = validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    
    @Override
    public void validate(T.value) throws ValidationException {
        Set<CostraintViolation<T>> constranitViolations = validator.valdate(value);
        if(constranitCiolations.size()>0) {
            StringBuilder message = new StringBuilder();
            for(CostraintViolation<T> constranitViolation : constranitViolations) {
                message.append(constranitViolation.getMessage()+"\n");
            }
            throw new ValidationException(message.toString());
        }
    }
}
```
在定义我们的MyItemProcessor时必须将MyBeanValidator设置进去，代码如下：
```java
@Bean
public ItemProcessor<Person,Person> processor() {
    MyItemProcessor processor = new MyItemProcessor();
    processor.setValidator(myBeanValidator());
    return process;
}

@Bean
public Validator<Person> myBeanValidator() {
    return new MyBeanValidator<Person>();
}
```
6.数据输出
Spring Batch为我们提供了大量的ItemWriter的实现，用来将数据输出到不同的目的地
7.计划任务
Spring Batch的任务是通过JobLauncher的run方法来执行的，因此我们只需在普通的计划任务方法中执行JobLauncher的run方法即可
演示代码如下，别忘了配置类使用@EnableScheduling开启计划任务支持：
```java
@Service
public class ScheduledTaskService {
    @Autowired
    JobLauncher jobLauncher;
    
    @Autowired
    Job importJob;
    
    public JobParameters jobParameters;
    
    @Scheduled(fixedRate = 5000)
    public void execute() throws Exception {
        jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(importJob,jobParameters);
    }
}
```
8.参数后置绑定
我们在ItemReader和ItemWriter的Bean定义的时候，参数已经硬编码在Bean的初始化中，代码如下：
```java
@Bean
public ItemReader<Person> reader() throws Excption {
    FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
    reader.setResource(new ClassPathResource("people.csv"));
    return reader;
}
```
这时我们要读取的文件的位置已经硬编码在Bean的定义中，这在很多情况下不符合我们的实际需求，这时我们需要使用参数后置绑定。
要实现参数后置绑定，我们可以在JobParameters中绑定参数，在Bean定义的时候使用一个特殊的Bean生命周期注解@StepScope,然后通过@Value注入
此参数
参数设置：
String path = "peopel.csv";

JobParameters jobParameters = new JobParametersBuilder().
         .addLong("time",System.currentTimeMillis())
          .addString("input.file.name",path)
          .toJobParameters();
jobLauncher.run(importJob,jobParameters);
定义Bean
```java
@Bean
@StepScope
public ItemReader<Person> reader(@Value("#{jobParameters['input.file.name']}") String puatToFile) throws Exception {
     FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
    reader.setResource(new ClassPathResource(puatToFile));
    return reader;
}
```         
### Spring Boot的支持
Spring Boot对Spring Batch支持的源码位于org.springframework.boot.autoconfigure.batch下
Spring Boot为我们自动初始化了Spring Batch存储批处理记录的数据库，且当我们程序启动时，会自动执行我们定义的Job的Bean
Spring Boot提供如下属性来定制Spring Batch:
spring.batch.job.names=job1,job2#启动时要执行的Job，默认执行全部Job
spring.batch.job.enabled=true#是否自动执行定义的Job,默认为是
spring.batch.initializer.enabled=true#是否初始化Spring Batch的数据库,默认为是
spring.batch.schema=
spring.batch.table-prefix=#设置Spring Batch的数据库表的前缀
### 9.2.3 实战
本例将使用Spring Batch将csv文件中的数据使用JDBC批处理的方式插入数据库。
1.新建Spring Boot项目
spring-batch-demo
依赖JDBC(spring-boot-starter-jdbc)、Batch(spring-boot-starter-batch)、Web(spring-boot-starter-web)
使用Oracle驱动，Spring Batch会自动加载hsqldb驱动，所以我们要去除
添加hibernate-validator依赖，作为数据校验使用
测试csv数据，位于src/main/resources/people.csv
数据表定义，位于src/main/resources/schema.sql中
数据源的配置和之前一样
2.领域模型类
Person
3.数据处理及校验
（1）处理：
CsvItemProcessor
（2）校验：
CsvBeanValidator
4.Job监听
CsvJobListener
5.配置
CsvBatchConfig
6.运行
启动程序，Spring Boot会自动初始化Spring Batch数据库，并将csv中的数据导入到数据库中
7.手动触发任务
很多时候批处理任务是人为触发的，在此我们添加一个控制器，通过人为触发批处理任务，并演示参数后置绑定的使用
注释掉CsvBatchConfig类的@Configuration注解，让此配置类不在其起效。新建TriggerBatchConfigp配置类，内容与CsvBatchConfig
完全保持一致，除了修改定义ItemReader这个Bean，ItemReader修改后的定义如下：
TriggerBatchConfig
控制器定义如下：
DemoController
此时我们还要关闭Spring Boot为我们自动执行Job的配置，在applicaiton.properties里使用下面代码关闭配置：
spring.batch.job.enabled=false
此时我们访问http://localhost:8080/imp?fileName=people,可获得相同的数据导入效果


           
























