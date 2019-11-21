package cn.andy.springmvc.web;

import cn.andy.springmvc.domain.DemoObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 11:21
 * @Description:
 */
//@Controller注解声明此类是一个控制器
@Controller
//@RequestMapping("/anno")映射此类的访问路径是/anno
@RequestMapping("/anno")
public class DemoAnnoConroller {

    //此方法未标注路径，因此使用类级别的路径/anno;produces可定制返回的response的
    //媒体类型和字符集，或需返回值是json对象，则设置produces="application/json;charset=UTF-8",
    @RequestMapping(produces = "text/plain;charset=UTF-8")
    public @ResponseBody String index(HttpServletRequest request) {//可接受HttpServletRequest作为参数，当然也可以接受HttpServletReponse作为参数
        return "url:"+request.getRequestURI()+" can access";
    }

    //接受路径参数，并在方法参数前结合@Pathvariable使用，访问路径为/anno/pathvar/xx
    @RequestMapping(value="/pathvar/{str}",produces = "text/plain;charset=UTF-8")
    public @ResponseBody String demoPathVar(@PathVariable String str,
                                            HttpServletRequest request) {
        return "url:"+request.getRequestURI()+" can access,str: "+str;
    }

    //常规的request参数获取，访问路径为/anno/requestParam?id=1
    @RequestMapping(value="/requestParam",produces = "text/plain;charset=UTF-8")
    public @ResponseBody String passRequestParam(Long id,HttpServletRequest request) {
        return "url:"+request.getRequestURI()+" can access,id: "+id;
    }

    //演示解释参数到对象，访问路径为/anno/obj?id=1&name=xx
    @RequestMapping(value="/obj",produces = "text/plain;charset=UTF-8")
    //@ResponseBody也可以用在方法上
    @ResponseBody
    public String passObj(DemoObj obj,HttpServletRequest request) {

        return "url:"+request.getRequestURI()+" can access,obj id: "+obj.getId()+" obj name:"+obj.getName();
    }

    //演示映射不同的路径到相同的方法，访问路径为/anno/name1或/anno/name2
    @RequestMapping(value={"/name1","/name2"},produces = "text/plain;charset=UTF-8")
    public @ResponseBody  String remove(HttpServletRequest request) {
        return "url:"+request.getRequestURI()+" can access";
    }


}
