package andy.aop;

import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 10:49
 * @Description: 编写使用方法规则被拦截类
 */
@Service
public class DemoMethodService {
    public void add(){
        System.out.println("DemoMethodService->add");
    }
}
