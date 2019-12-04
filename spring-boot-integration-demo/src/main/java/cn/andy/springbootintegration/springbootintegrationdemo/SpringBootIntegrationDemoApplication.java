package cn.andy.springbootintegration.springbootintegrationdemo;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.scheduling.PollerMetadata;

import java.io.File;
import java.io.IOException;

import static java.lang.System.getProperty;

@SpringBootApplication
public class SpringBootIntegrationDemoApplication {

    //1通过@value注解自动获得https://spring.io/blog.atom的资源
    @Value("https://spring.io/blog.atom")
    Resource resource;

    //2使用FluentAPI和Pollers配置默认的轮询方式
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(500).get();
    }

    //3FeedEntryMessage实际为feed:inbound-channel-adapter，此处即构造feed的入站通道适配器作为数据输入
    @Bean
    public FeedEntryMessageSource feedMessageSource() throws IOException {
        FeedEntryMessageSource messageSource =
                new FeedEntryMessageSource(resource.getURL(), "news");
        return messageSource;
    }


    @Bean
    public IntegrationFlow myFlow() throws IOException {
        return IntegrationFlows.from(feedMessageSource())//4流程从from方法开始
                //5通过路由方法route来选择路由，消息体(payload)的类型为SyndEntry,作为判断条件的类型为String，判断的值是通过payload
                //获得的分类(Categrou)
                .<SyndEntry, String>route(payload ->
                                payload.getCategories().get(0).getName(),
                        //6通过不同分类的值转向不同的消息通道，若分类为release,则转向releasesChannel;
                        //若分类为engineering,则转向engineeringChannel;
                        //若分类为news,则转向newsChannel;
                        mapping -> mapping.channelMapping("releases",
                                "releasesChannel")
                                .channelMapping("engineering", "engineeringChannel")
                                .channelMapping("news", "newsChannel")
                ).get();//7通过get方法获得IntegrationFlow实体，配置为Spring的Bean

    }

    @Bean
    public IntegrationFlow releasesFlow() {
        //1从消息通道releasesChannel开始获取数据
        return IntegrationFlows.from(MessageChannels.queue("releasesChannel",
                10))
                //2使用transform方法进行数据转换。payload类型为SyndEntry,将其转换为字符串类型，并自定义数据的格式
                .<SyndEntry, String>transform(
                        payload -> "《" + payload.getTitle() + "》" +
                                payload.getLink() + getProperty("line.separator"))
                //3用handle方法处理file的出战适配器。Files类是由Spring Integration Java DSL提供的FluentAPI用来构造这个文件输出适配器
                .handle(Files.outboundAdapter(new File("F:/springblog"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .charset("UTF-8")
                        .fileNameGenerator(message -> "releases.txt")
                        .get())
                .get();
    }

    @Bean
    public IntegrationFlow engineeringFlow() {
        return IntegrationFlows.from(MessageChannels.queue("engineeringChannel", 10))
                .<SyndEntry, String>transform(
                        e -> "《" + e.getTitle() + "》" + e.getLink() + getProperty("line.separator"))
                .handle(Files.outboundAdapter(new File("F:/springblog"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .charset("UTF-8")
                        .fileNameGenerator(message -> "engineering.txt")
                        .get())
                .get();
    }

    @Bean
    public IntegrationFlow newsFlow() {
        return IntegrationFlows.from(MessageChannels.queue("newsChannel", 10))
                .<SyndEntry, String>transform(
                        payload -> "《" + payload.getTitle() + "》" + payload.getLink() + getProperty("line.separator"))
                //1通过enricherHeader来增加消息头的信息
                .enrichHeaders(
                        Mail.headers()
                                .subject("来自Spring的新闻")
                                .to("zhuwei927@163.com")
                                .from("zhuwei927@163.com").get())
                //2 邮件发送的相关信息通过Spring Integration Java DSL提供的Mail的headers方法来构造
                //使用handle方法来定义邮件发送的出站适配器，使用Spring Integration Java DSL提供的Mail.outboundAdapater
                //来构造。
                .handle(Mail.outboundAdapter("stmp.126.com")
                        .port(25)
                        .protocol("smtp")
                        .credentials("zhuwei927@163.com", "xxx")
                        .javaMailProperties(p -> p.put("mail.debug", "false")),
                        e -> e.id("smtpOut")).get();
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringBootIntegrationDemoApplication.class, args);
    }


}
