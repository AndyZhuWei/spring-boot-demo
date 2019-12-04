package cn.andy.springbatch.batch;

import cn.andy.springbatch.domain.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Author: zhuwei
 * @Date:2019/11/28 11:00
 * @Description:
 */
//@Configuration
//开启批处理的支持
@EnableBatchProcessing
public class CsvBatchConfig {

    //ItemReader定义
    @Bean
    public ItemReader<Person> reader() throws Exception {
        //1使用FlatFileItemReader读取文件
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        //2使用FlatFileItemReader的setResource方法设置csv文件的路径
        reader.setResource(new ClassPathResource("people.csv"));
        //3在此处对cvs文件的数据和领域模型类做对应映射
        reader.setLineMapper(new DefaultLineMapper<Person>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[]{"name","age","nation","address"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>(){{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    //ItemProcess定义
    @Bean
    public ItemProcessor<Person,Person> processor() {
        //1使用我们自定义的ItemProcessor的实现CsvItemProcessor
       CsvItemProcessor processor = new CsvItemProcessor();
        //2为processor指定校验器CsvBeanValidator
        processor.setValidator(csvBeanValidator());
        return processor;
    }

    //ItemWriter定义
    @Bean
    public ItemWriter<Person> writer(DataSource dataSource) {//1Spring能让容器中已有的Bean以参数的形式注入，Spring Boot已为我们定义了dataSource
        //2我们使用JDBC批处理的jdbcBatchItemWriter来写数据到数据库
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new
                BeanPropertyItemSqlParameterSourceProvider<Person>());
        String sql = "insert into person " + "(id,name,age,nation,address) "
                +
                "values(hibernate_sequence.nextval,:name,:age,:nation,:address)";
        //3在此设置要执行批处理的SQL语句
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }

    //JobRepository定义
    //jobRepository的定义需要dataSource和transactionManager，Spring Boot已为我们自动
    //配置了这两个类，Spring可通过方法注入已有的Bean
    @Bean
    public JobRepository jobRepository(DataSource dataSource,
          PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new
                JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType("oracle");
        return jobRepositoryFactoryBean.getObject();

    }

    //JobLauncher定义
    @Bean
    public SimpleJobLauncher jobLauncher(DataSource dataSource,
                    PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository(dataSource,transactionManager));
        return jobLauncher;
    }

    //Job定义
    @Bean
    public Job importJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                //1为Job指定Step
                .flow(s1)
                .end()
                //2绑定监听器csvJobListener
                .listener(csvJobListener())
                .build();
    }

    //Step定义
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader<Person> reader,
                      ItemWriter<Person> writer,ItemProcessor<Person,Person> processor) {
        return stepBuilderFactory
                .get("step1")
                //1批处理每次提交65000条数据
                .<Person,Person>chunk(65000)
                //2给step绑定reader
                .reader(reader)
                //3给step绑定processor
                .processor(processor)
                //4给step绑定writer
                .writer(writer)
                .build();
    }

    @Bean
    public CsvJobListener csvJobListener() {
        return new CsvJobListener();
    }

    @Bean
    public Validator<Person> csvBeanValidator() {
        return new CsvBeanValidator<Person>();
    }


































}
