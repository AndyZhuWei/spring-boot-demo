package cn.andy.springBootDemo.domain;

/**
 * @Author: zhuwei
 * @Date:2018/10/26 11:15
 * @Description:
 */
public class WiselyResponse {
    private String responseMessage;

    public WiselyResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
