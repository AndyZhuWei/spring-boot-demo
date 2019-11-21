package cn.andy.springdatajpa.springdatajpademo.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @Author: zhuwei
 * @Date:2019/11/19 22:48
 * @Description: 此例中的接口继承了JpaRepository，让我们具备了JpaRepository所提供的方法
 * 继承了JpaSpecificationExecutor,让我们具备使用Specification的能力
 */
@NoRepositoryBean//注解的接口不会被单独创建实例，只会作为其他接口的父接口而被使用
public interface CustomRepository<T,ID extends Serializable> extends
        JpaRepository<T,ID>, JpaSpecificationExecutor<T> {
    Page<T> findByAuto(T example, Pageable pageable);
}
