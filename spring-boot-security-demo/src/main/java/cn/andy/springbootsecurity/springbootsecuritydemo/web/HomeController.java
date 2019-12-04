package cn.andy.springbootsecurity.springbootsecuritydemo.web;

import cn.andy.springbootsecurity.springbootsecuritydemo.domain.Msg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: zhuwei
 * @Date:2019/11/27 17:47
 * @Description:
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) {
        Msg msg = new Msg("测试标题","测试内容","额外信息，只对管理员显示");
        model.addAttribute("msg",msg);
        return "home";
    }
}
