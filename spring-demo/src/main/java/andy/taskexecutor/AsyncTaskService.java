package andy.taskexecutor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 16:16
 * @Description:
 */
@Service
public class AsyncTaskService {

    //通过@Async注解表明该方法是一个异步方法，如果注解在类级别，则表明该类所有的
    //方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor作为taskExecutor
    @Async
    public void executeAsyncTask(Integer i) {
        System.out.println("执行异步任务："+i);
    }

    @Async
    public void executeAsyncTaskPlus(Integer i) {
        System.out.println("执行异步任务+1"+(i+1));
    }
}
