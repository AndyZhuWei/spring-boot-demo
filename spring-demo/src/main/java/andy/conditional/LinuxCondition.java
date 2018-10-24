package cn.andy.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:27
 * @Description: 判定Linux的条件
 */
public class LinuxCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return context.getEnvironment().getProperty("os.name").contains("Linux");
    }
}
