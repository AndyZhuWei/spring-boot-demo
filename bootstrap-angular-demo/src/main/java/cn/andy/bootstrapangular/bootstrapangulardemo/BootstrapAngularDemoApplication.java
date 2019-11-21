package cn.andy.bootstrapangular.bootstrapangulardemo;

import cn.andy.bootstrapangular.bootstrapangulardemo.model.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BootstrapAngularDemoApplication {

    //这里我们只是模拟了一个查询，即接受前台传入的personName，然后返回Person类，因为我们使用的是
    //@RestController，且返回值类型是Person,所以Spring MVC会自动将对象输出为JSON
    @RequestMapping(value="/search",produces = {MediaType.APPLICATION_JSON_VALUE})
    public Person search(String personName) {
        return new Person(personName,32,"hefei");
    }

    public static void main(String[] args) {
        SpringApplication.run(BootstrapAngularDemoApplication.class, args);
    }

}
