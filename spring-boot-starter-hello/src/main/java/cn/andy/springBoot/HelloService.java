package cn.andy.springBoot;

//根据此类的存在与否来创建这个类的Bean,这个类可以是第三方类库的类
public class HelloService {

    private String msg;

    public String sayHello() {
        return "Hello "+msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
