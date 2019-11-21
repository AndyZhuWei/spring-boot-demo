package cn.andy.springBoot;

import org.springframework.boot.context.properties.ConfigurationProperties;

//l.类型安全的属性获取
@ConfigurationProperties(prefix = "hello")
public class HelloServiceProperties {
    private static final String MSG = "world";

    private String msg = MSG;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
