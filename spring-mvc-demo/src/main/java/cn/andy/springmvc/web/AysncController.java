package cn.andy.springmvc.web;

import cn.andy.springmvc.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 17:18
 * @Description: 异步任务的实现是通过控制器从另外一个线程返回一个DeferredResult,
 * 这里的DeferredResult是从pushService中获得的
 */
@Controller
public class AysncController {

    //定时任务，定时更新DeferredResult
    @Autowired
    PushService pushService;

    //返回给客户端DeferredResult
    @RequestMapping("/defer")
    @ResponseBody
    public DeferredResult<String> deferredCall() {
        return pushService.getAsyncUpdate();
    }
}
