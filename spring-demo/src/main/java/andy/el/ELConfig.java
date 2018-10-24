package cn.andy.el;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 11:36
 * @Description:
 */
@Configuration
@ComponentScan("cn.andy.el")
//注入配置文件需要使用@PropertySource指定文件地址，若使用@Value注入，则要配置
//一个PropertySourcesPlaceholderConfigurer的Bean
@PropertySource("classpath:cn/andy/el/test.properties")
public class ELConfig {

    //注入普通字符串
    @Value("I Love You!")
    private String normal;

    //注入操作系统属性
    @Value("#{systemProperties['os.name']}")
    private String osName;

    //注入表达式结果
    @Value("#{T(java.lang.Math).random()*100.0}")
    private double randomNumber;

    //注入其他Bean属性
    @Value("#{demoService.another}")
    private String fromAnother;

    //注入文件资源
    @Value("classpath:cn/andy/el/test.txt")
    private Resource testFile;

    //注入网址资源
    @Value("http://www.baidu.com")
    private Resource testUrl;

    //注入配置文件,注意，@Value中使用的是$而不是#
    @Value("${book.name}")
    private String bookName;

    //注入Properties还可从Environment中获得
    @Autowired
    private Environment environment;

    //注入配置文件时，并且通过@Value,则需要配置这个bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputResource() {
        try {
            System.out.println(normal);
            System.out.println(osName);
            System.out.println(randomNumber);
            System.out.println(fromAnother);
            System.out.println(IOUtils.toString(testFile.getInputStream()));
            System.out.println(IOUtils.toString(testUrl.getInputStream()));
            //经测试发现没有输出bookName的值
            System.out.println(bookName);
            System.out.println(environment.getProperty("book.author"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
