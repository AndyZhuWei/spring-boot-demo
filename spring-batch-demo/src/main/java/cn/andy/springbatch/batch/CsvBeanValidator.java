package cn.andy.springbatch.batch;

import cn.andy.springbatch.domain.Person;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @Author: zhuwei
 * @Date:2019/11/28 10:47
 * @Description:
 */
public class CsvBeanValidator<T> implements Validator<T>, InitializingBean {
    private javax.validation.Validator validator;

    //1使用JSR-303的Validator来校验我们的数据，在此处进行JSR-303的Validator的初始化。
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory =
                Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Override
    public void validate(T value) throws ValidationException {
        //2使用Validator的validate方法校验数据
        Set<ConstraintViolation<T>> constraintViolations =
                validator.validate(value);
        if(constraintViolations.size()>0) {
            StringBuilder message = new StringBuilder();
            for(ConstraintViolation<T> constraintViolation : constraintViolations) {
                message.append(constraintViolation.getMessage()+"\n");
            }
            throw new ValidationException(message.toString());
        }

    }































}
