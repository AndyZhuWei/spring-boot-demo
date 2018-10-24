package cn.andy.fortest;

import andy.fortest.TestBean;
import andy.fortest.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//SpringJUnit4ClassRunne在JUnit环境下提供Spring TestContext Framework的功能
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration用来加载配置ApplicationContext，其中classes属性用来加载配置类
@ContextConfiguration(classes = {TestConfig.class})
//@ActiveProfiles用来声明活动的profile
@ActiveProfiles("prod")
public class DemoBeanIntegrationTests {

    //可使用普通的@Autowired注入Bean
    @Autowired
    private TestBean testBean;

    //测试代码，通过JUnit的Assert来校验结果是否和预期一致
    @Test
    public void prodBeanShouldInject() {
        String expected = "from production profile";
        String actual = testBean.getContent();
        Assert.assertEquals(expected,actual);
    }
}
