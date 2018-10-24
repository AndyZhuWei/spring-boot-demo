package andy.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 9:48
 * @Description:
 */
@Service
public class UseFunctionService {

    //使用@Autowired将FunctionService的实体Bean注入到UseFunctionService中，
    //让UserFunctionService具备FunctionService的功能，此处使用JSR-330的@Inject注解或JSR-250
    //的@Resource注解是等效的
    @Autowired
    FunctionService functionService;

    public String sayHello(String word) {
        return functionService.sayHello(word);
    }
}
