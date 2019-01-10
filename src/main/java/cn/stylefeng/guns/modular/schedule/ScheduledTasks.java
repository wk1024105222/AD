package cn.stylefeng.guns.modular.schedule;

import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.service.IAttendanceService;
import cn.stylefeng.guns.modular.system.service.IMonthAttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ScheduledTasks {

    @Autowired
    private IAttendanceService attendanceRecordService;

    @Autowired
    private IMonthAttendanceService monthAttendanceService;

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    //每周一11点
    @Scheduled(cron = "0 0 11 ? * MON")
//    @Scheduled(cron = "0/1 * * * * ?")
    public void everyWeekSendMail(){

        String[] to = {"1024105222@qq.com","david_wang@comwave.com.cn"};
        String filePath = "E:/WeChat Files/shengji310065/Files/51还款测试案例--0109(3).xlsx";
        PersonUtil.sendMail(mailSender,"18565430729@163.com",to,"发多人","测试邮件内容",new File(filePath), "月度考勤表");
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

    public void runButch(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        List<AttendanceRecord> ads =  attendanceRecordService.getOneDayAttendRecords(today);
        if(ads.size()<2) {
            return ;
        }
        int begin = 0;
        for(int i=1; i!=ads.size(); i++) {
            if(!ads.get(i).getUserId().equals(ads.get(i-1).getUserId())) {
                attendanceRecordService.handleSomeOneAttendRecord(new LinkedList(ads.subList(begin, i)));
                begin = i;
            }
        }

        List<Map<String, Object>> statisticsRlt = attendanceRecordService.statisticsOneDayAttendRecords(today);
        for(Map<String, Object> tmp : statisticsRlt) {
            String userId = (String) tmp.get("user_id");
            String note = ((String) tmp.get("result")).replace(",","</br>");
            String year = today.substring(0,4);
            String month = today.substring(5,7);
            String day = today.substring(8);
            if(day.startsWith("0")) {
                day = day.substring(1);
            }
            MonthAttendance m = monthAttendanceService.getMonthAttendanceByYearMonthUserId(year,month,userId);
            if(m == null) {
                m = new MonthAttendance(UUID.randomUUID().toString().replaceAll("-", ""),year,month,userId);
            }

            Class reflect = m.getClass();
            try {
                Method setDayN= reflect.getMethod("setDay"+day,String.class);
                setDayN.invoke(m,note);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            boolean a = monthAttendanceService.insertOrUpdate(m);
        }
    }

}
