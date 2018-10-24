package andy.prepost;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 13:48
 * @Description:
 */
public class JSR250WayService {

    //@PostConstruct表示在构造函数执行完之后执行
    @PostConstruct
    public void init() {
        System.out.println("jsr250-init-method");
    }

    public JSR250WayService(){
        super();
        System.out.println("初始化构造函数JSR250WayService");
    }

    //@PreDestroy 在Bean销毁之前执行
    @PreDestroy
    public void destroy(){
        System.out.println("jsr250-destory-method");
    }
}
