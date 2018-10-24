package cn.andy.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 11:25
 * @Description:
 */
@Service
@Scope("prototype")
public class DemoPrototypeService {
}
