package andy.taskscheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:03
 * @Description: Spring通过@Scheduled支持多种类型的计划任务，包含cron\fixDelay\fixRate
 */
@Service
public class ScheduledTaskService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("每隔五秒执行一次 "+dateFormat.format(new Date()));
    }

    //使用cron属性可按照指定时间执行，本例指的是每天11点28分执行，cron是UNIX
    //和类UNIX(Linux)系统下的定时任务
    @Scheduled(cron="0 16 09 ? * *")
    public void fixTimeExecution() {
        System.out.println("在指定时间 "+dateFormat.format(new Date())+"执行");
    }
}
