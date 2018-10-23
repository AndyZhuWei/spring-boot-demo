package cn.andy.el;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 11:35
 * @Description:
 */
@Service
public class DemoService {

    //注入普通字符串
    @Value("其他类的属性")
    private String another;

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }
}
