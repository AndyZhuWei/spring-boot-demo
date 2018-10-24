package andy.javaconfig;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 10:15
 * @Description:
 */
public class UseFunctionService {

    FunctionService functionService;

    public void setFunctionService(FunctionService functionService) {
        this.functionService = functionService;
    }

    public String sayHello(String word){
        return functionService.sayHello(word);
    }

}
