package cn.andy.springBoot;

import cn.andy.springBoot.cn.andy.springBoot.config.AuthorSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@SpringBootApplicaiton是Spring Boot项目的核心注解，主要目的是开启自动配置。
@SpringBootApplication
//可以通过@ImportResource的方式导入xml配置文件
//@ImportResource({"classpath:xx","classpath:yy"})
public class Application {

	@Value("${book.author}")
	private String bookAuthor;

	@Value("${book.name}")
	private String bookName;

	@Autowired
	private AuthorSettings authorSettings;

	@Autowired
	private HelloService helloService;

	@RequestMapping("/")
	String index() {
//		return "Hello Spring Boot";
//		return "book name is:"+bookName+" and book author is:"+bookAuthor;
//		return "author name is:"+authorSettings.getName()+" and author age is:"+authorSettings.getAge();
		return helloService.sayHello();
	}


	//这是一个标准的Java应用的main方法，主要作用是作为项目启动的入口
	public static void main(String[] args) {
		//SpringApplication.run(Application.class, args);
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
