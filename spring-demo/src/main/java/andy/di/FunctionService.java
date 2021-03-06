package andy.di;

import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 9:45
 * @Description: 其中，使用@Component、@Service、@Repository和@Controller是等效的
 */
//使用@Service注解声明当前FunctionService类是Spring管理的一个Bean.
@Service
public class FunctionService {
    public String sayHello(String word) {
        return "Hello "+ word +"!";
    }
}
