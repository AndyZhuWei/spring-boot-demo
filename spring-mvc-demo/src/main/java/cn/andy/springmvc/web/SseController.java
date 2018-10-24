package cn.andy.springmvc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 16:37
 * @Description: 基于SSE(Server Send Event服务端发送事件)的服务器端推送，这种方式推送需要新式浏览器的支持
 */
@Controller
public class SseController {

    //这里使用输出的媒体类型为text/event-stream，这是服务器端SSE的支持
    @RequestMapping(value="/push",produces = "text/event-stream")
    public @ResponseBody  String push() {
        Random r = new Random();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "data:Testing 1,2,3"+r.nextInt()+"\n\n";
    }
}
