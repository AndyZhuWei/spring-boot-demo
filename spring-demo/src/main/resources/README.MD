## 3.6 @Enable*注解的工作原理

在本章的第一部分我们通过：
@EnableAspectJAutoProxy开启了对AspectJ自动代理的支持
@EnableAsync开启异步方法的支持
@EnableScheduling开启计划任务的支持

在第二部分我们通过：
@EnableWebMvc开启Web MVC的配置支持

在第三部分我们通过：
@EnableConfigurationProperties开启对@ConfigurationProperties注解配置Bean的支持
@EnableJpaRepositories开启对Spring Data JPA Repository的支持
@EnableTransactionManagement开启注解式事务的支持
@EnableCaching开启注解式的换成支持

通过简单的@Enable*来开启一项功能的支持，从而避免自己配置大量的代码，大大降低使用难度。那么这个神奇的功能的实现原理是什么呢？

通过观察这些@Enable*注解的源码，我们发现所有的注解都有一个@Import注解，@Import是专门用来导入配置的，这也就意味着这些自动开启的实现其实是导入了
一些自动配置的Bean。这些导入的配置方式主要分为以下三种类型

### 3.6.1 第一类：直接导入配置类
例如：
@EnableScheduling
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SchedulingConfiguration.class})
@Documented
public @interface EnableScheduling {
}
```
直接导入了配置类SchedulingConfiguration
```java
@Configuration
@Role(2)
public class SchedulingConfiguration {
    public SchedulingConfiguration() {
    }

    @Bean(
        name = {"org.springframework.context.annotation.internalScheduledAnnotationProcessor"}
    )
    @Role(2)
    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }
}
```
这个类注解了@Configuration，且注册了一个scheduledAnnotationProcessor的Bean

### 3.6.2 第二类：依据条件选择配置类
例如：
@EnableAsync
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AsyncConfigurationSelector.class})
public @interface EnableAsync {
    Class<? extends Annotation> annotation() default Annotation.class;

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;

    int order() default 2147483647;
}
```
AsyncConfigurationSelector通过条件来选择需要导入的配置类，AsyncConfigurationSelector的根接口为ImportSelector,这个接口需要重写
selectImports方法，在此方法内进行事先条件判断。
```java
public class AsyncConfigurationSelector extends AdviceModeImportSelector<EnableAsync> {
    private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";

    public AsyncConfigurationSelector() {
    }

    public String[] selectImports(AdviceMode adviceMode) {
        switch(adviceMode) {
        case PROXY:
            return new String[]{ProxyAsyncConfiguration.class.getName()};
        case ASPECTJ:
            return new String[]{"org.springframework.scheduling.aspectj.AspectJAsyncConfiguration"};
        default:
            return null;
        }
    }
}
```
此例中，若adviceMode为PORXY,则返回ProxyAsyncConfiguration这个配置类，若adviceMode为PASPECTJ，则返回AspectJAsyncConfiguration配置类

### 3.6.3 第三类：动态注册Bean
例如：
@EnableAspectJAutoProxy
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AspectJAutoProxyRegistrar.class})
public @interface EnableAspectJAutoProxy {
    boolean proxyTargetClass() default false;

    boolean exposeProxy() default false;
}
```
AspectJAutoProxyRegistrar实现了ImportBeanDefinitionRegistrar接口,ImportBeanDefinitionRegistrar的作用是在运行时自动添加Bean到已有
的配置类，通过重写方法：
registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
其中，AnnotationMetadata参数用来获得当前配置类上的注解；BeanDefinitionRegistry参数用来注册Bean
```java

class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {
    AspectJAutoProxyRegistrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
        AnnotationAttributes enableAspectJAutoProxy = AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
        if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
            AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
        }

        if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
            AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
        }

    }
}
```

## 3.7 测试
集成测试为我们提供了一种无须部署或运行程序来完成验证系统各部分是否正常协同工作的能力
Sprig 通过Spring TextContext Framework对集成测试提供顶级支持。它不依赖特定的测试框架，既可使用Junit,也可使用TestNG

Spring提供了一个SpringJUnit4ClassRunner类，它提供了Spring TestContext Framework的功能。通过@ContextConfiguration来配置
Application Context，通过@ActiveProfiles确定活动的profile


