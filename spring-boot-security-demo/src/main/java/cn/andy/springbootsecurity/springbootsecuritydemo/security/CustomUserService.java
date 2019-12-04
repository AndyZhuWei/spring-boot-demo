package cn.andy.springbootsecurity.springbootsecuritydemo.security;

import cn.andy.springbootsecurity.springbootsecuritydemo.dao.SysUserRepository;
import cn.andy.springbootsecurity.springbootsecuritydemo.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author: zhuwei
 * @Date:2019/11/27 16:16
 * @Description:
 */
//1自定义需实现UserDetailsService接口
public class CustomUserService implements UserDetailsService {

    @Autowired
    SysUserRepository userRepository;

    //2重写loadUserByUsername方法获得用户
    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        //3我们当前的用户实现了UserDetails接口，可直接返回给Spring Security使用。
        return user;
    }
}
