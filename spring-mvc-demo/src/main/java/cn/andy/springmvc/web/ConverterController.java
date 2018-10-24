package cn.andy.springmvc.web;

import cn.andy.springmvc.domain.DemoObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 16:08
 * @Description:
 */
@Controller
public class ConverterController {

    //指定返回的媒体类型为我们自定义的媒体类型application.x-wisely
    @RequestMapping(value="/convert",produces = {"application/x-wisely"})
    public @ResponseBody
    DemoObj convert(@RequestBody DemoObj demoObj) {
        return demoObj;
    }
}
