package cn.andy.springmvc.web;

import cn.andy.springmvc.domain.DemoObj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 13:44
 * @Description:
 */
//声明@RestController，声明是控制器，并且返回数据时不需要@ResponseBody
@RestController
@RequestMapping("/rest")
public class DemoRestController {

    //返回数据的媒体类型为json
    //直接返回对象，对象会自动转换成json
    @RequestMapping(value="/getjson",produces = "application/json;charset=UTF-8")
    public DemoObj getjson(DemoObj obj) {
        return new DemoObj(obj.getId()+1,obj.getName()+"yy");
    }

    //返回数据的媒体类型为xml
    //直接返回对象，对象会自动转换成xml
    @RequestMapping(value="/getxml",produces = "application/xml;charset=UTF-8")
    public DemoObj getxml(DemoObj obj) {
        return new DemoObj(obj.getId()+1,obj.getName()+"yy");
    }
}
