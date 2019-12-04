package cn.andy.springbatch.batch;

import cn.andy.springbatch.domain.Person;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * @Author: zhuwei
 * @Date:2019/11/28 10:43
 * @Description:
 */
public class CsvItemProcessor extends ValidatingItemProcessor<Person> {

    @Override
    public Person process(Person item) throws ValidationException {
        //1.需执行super.process(item)才会调用自定义校验器
        super.process(item);

        //2对数据做简单的处理，若民族为汉族，则数据转换为01，其余转换成02
        if(item.getNation().equals("汉族")) {
           item.setNation("01");
        } else {
            item.setNation("02");
        }

        return item;
    }










































}
