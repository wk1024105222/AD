package cn.stylefeng.guns.modular.schedule;

import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.model.Dept;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.service.IAttendanceService;
import cn.stylefeng.guns.modular.system.service.IDeptService;
import cn.stylefeng.guns.modular.system.service.IMonthAttendanceService;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Configuration
@EnableScheduling // 启用定时任务
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    @Autowired
    private IAttendanceService attendanceRecordService;
    @Autowired
    private IMonthAttendanceService monthAttendanceService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IDeptService deptService;

    //每周一11点
//    @Scheduled(cron = "0 0 11 ? * MON")
    public void everyWeekSendMail() {

        String[] to = {"1024105222@qq.com", "david_wang@comwave.com.cn"};
        String filePath = "E:/WeChat Files/shengji310065/Files/51还款测试案例--0109(3).xlsx";
        PersonUtil.sendMail(mailSender, "18565430729@163.com", to, "发多人", "测试邮件内容", new File(filePath), "月度考勤表");
        logger.info("everyWeekSendMail run");
    }

    //每天10点
    @Scheduled(cron = "0 0 10 * * ?")
    public void everyDaySendMail() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
        int year = yesterday.getYear();
        int month = yesterday.getMonthValue();

        logger.info("everyDaySendMail run");
        String cycle  = "1";
        List<Dept> depts = deptService.getDeptBySendEmailCycle(cycle);

        for(Dept d : depts) {
            List<MonthAttendance> ads = monthAttendanceService.getMonthAttendanceByYearMonthDeptId(year,month, d.getId());
            if(ads.size()>0) {
                String fileName= d.getSimplename()+yesterday.format(dtf)+"月度考勤表.xls";
                File f = new File(fileName);
                if(f.exists()) {
                    f.delete();
                }
                boolean a = monthAttendanceService.exportMonthAttendanceReportXls(ads,fileName);

                if(f.exists()) {
                    List<String> toEmails = new ArrayList<String>() ;
                    if(d.getEmail1()!=null) {toEmails.add(d.getEmail1());}
                    if(d.getEmail2()!=null) {toEmails.add(d.getEmail2());}
                    if(d.getEmail3()!=null) {toEmails.add(d.getEmail3());}
                    String[] to = new String[toEmails.size()];
                    for(int i=0;i!=to.length;i++) {
                        to[i]=toEmails.get(i);
                    }
                    PersonUtil.sendMail(mailSender, "18565430729@163.com", to, fileName, "", new File(fileName), yesterday.format(dtf)+".xls");

                }

            }
        }
    }




    //每月1日12点
    @Scheduled(cron = "0 0 12 1 * ?")
    public void everyMonthSendMail() {
        logger.info("everyMonthSendMail run");
    }
//
//    @Scheduled(fixedRate = 1000)
//    public void reportCurrent() {
//        SimpleDateFormat dataFromat = new SimpleDateFormat("HH:mm:ss");
//        logger.info("现在时间：{}", dataFromat.format(new Date()));
//    }

    //每天2点跑批
    @Scheduled(cron = "0 0 2 ? * *")
    public void runButch() {
        logger.info("runButch run");

        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);

        boolean markRlt = attendanceRecordService.markAttendanceRecord(yesterday);

        boolean  statisticsRlt = attendanceRecordService.statisticsOneDayAttendRecords(yesterday);

    }

}
