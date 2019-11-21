package cn.andy.springdatajpa.springdatajpademo.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static cn.andy.springdatajpa.springdatajpademo.specs.CustomerSpecs.byAuto;

/**
 * @Author: zhuwei
 * @Date:2019/11/19 22:52
 * @Description: 此类继承JpaRepository的实现类SimpleJpaRepository，让我们可以使用SimpleJpaRepository的方法；此类
 * 当然还要实现我们自定义的接口CustomeRepository
 */
public class CustomRepositoryImpl<T,ID extends Serializable>
    extends SimpleJpaRepository<T,ID> implements CustomRepository<T,ID> {

    private Class<T> domainClass;
    private final EntityManager entityManager;

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass,entityManager);
        this.domainClass = domainClass;
        this.entityManager=entityManager;
    }

    /**
     * findByAuto方法使用byAuto Secification构造的条件查询，并提供分页功能
     * @param example
     * @param pageable
     * @return
     */
    @Override
    public Page<T> findByAuto(T example, Pageable pageable) {
        return findAll(byAuto(entityManager,example),pageable);
    }
}

















































