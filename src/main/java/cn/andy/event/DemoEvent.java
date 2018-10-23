package cn.andy.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 14:23
 * @Description:
 */
public class DemoEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String msg;

    public DemoEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
