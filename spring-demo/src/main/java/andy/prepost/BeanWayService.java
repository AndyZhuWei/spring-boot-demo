package andy.prepost;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 13:44
 * @Description:
 */
public class BeanWayService {
    public void init() {
        System.out.println("@Bean-init-method");
    }

    public BeanWayService() {
        super();
        System.out.println("初始化构造函数-BeanWayService");
    }

    public void destroy() {
        System.out.println("@Bean-destory-method");
    }
}
