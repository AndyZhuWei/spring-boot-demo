package cn.andy.springmvc.messageconverter;

import cn.andy.springmvc.domain.DemoObj;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 15:40
 * @Description:
 */
//继承AbstractHttpMessageConverter接口来实现自定义的HttpmessageConverter
public class MyMessageConverter extends AbstractHttpMessageConverter<DemoObj> {

    //新建一个我们自定义的媒体类型application/x-wisely
    public MyMessageConverter() {
        super(new MediaType("application","x-wisely", Charset.forName("UTF-8")));
    }

    //表明本HttpMessageConverter只处理DemoObj这个类
    @Override
    protected boolean supports(Class<?> aClass) {
        return DemoObj.class.isAssignableFrom(aClass);
    }

    //重写readInternal方法，处理请求的数据。代码表明我们处理由“-”隔开的数据，并转成DemoObj的对象
    @Override
    protected DemoObj readInternal(Class<? extends DemoObj> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        String temp = StreamUtils.copyToString(httpInputMessage.getBody(),Charset.forName("UTF-8"));
        String[] tempArr = temp.split("-");
        return new DemoObj(new Long(tempArr[0]),tempArr[1]);
    }

    //重写writeInternal，处理如何输出数据到response。此例中，我们在原样输出前面加上“hello:”
    @Override
    protected void writeInternal(DemoObj demoObj, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        String out = "hello:"+ demoObj.getId()+"-"+demoObj.getName();
        httpOutputMessage.getBody().write(out.getBytes());
    }
}
