package cn.andy.springbootsecurity.springbootsecuritydemo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: zhuwei
 * @Date:2019/11/27 16:01
 * @Description:
 */
@Entity
public class SysRole {

    @Id
    @GeneratedValue
    private Long id;

    //1name为角色名称
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
