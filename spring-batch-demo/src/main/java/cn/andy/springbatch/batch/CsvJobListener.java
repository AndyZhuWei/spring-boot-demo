package cn.andy.springbatch.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @Author: zhuwei
 * @Date:2019/11/28 10:56
 * @Description: 监听器实现JobExecutionListener接口，并重写其beforeJob、afterJob方法即可
 */
public class CsvJobListener implements JobExecutionListener {

    long startTime;
    long endTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        System.out.println("任务开始处理");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        System.out.println("任务处理结束");
        System.out.println("耗时："+(endTime-startTime)+"ms");
    }


































}
