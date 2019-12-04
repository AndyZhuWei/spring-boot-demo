package cn.andy.springbootsecurity.springbootsecuritydemo.dao;

import cn.andy.springbootsecurity.springbootsecuritydemo.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhuwei
 * @Date:2019/11/27 16:14
 * @Description: 这里只需一个根据用户名查出用户的方法。
 */
public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    SysUser findByUsername(String username);
}
