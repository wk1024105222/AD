package cn.stylefeng.guns.modular.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    //每周一11点
    @Scheduled(cron = "0 0 11 ? * MON")
//    @Scheduled(cron = "0/1 * * * * ?")
    public void everyWeekSendMail(){
        logger.info("everyWeekSendMail run");
    }
    //每天10点
    @Scheduled(cron = "0 0 10 * * ?")
    public void everyDaySendMail(){
        logger.info("everyDaySendMail run");
    }
    //每月1日12点
    @Scheduled(cron = "0 0 12 1 * ?")
    public void everyMonthSendMail(){
        logger.info("everyMonthSendMail run");
    }

//    @Scheduled(fixedRate = 1000)
    public void reportCurrent(){
        SimpleDateFormat dataFromat = new SimpleDateFormat("HH:mm:ss");
        logger.info("现在时间：{}",dataFromat.format(new Date()));
    }

}
