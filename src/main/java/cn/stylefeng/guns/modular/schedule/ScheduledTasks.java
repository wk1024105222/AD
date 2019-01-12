package cn.stylefeng.guns.modular.schedule;

import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.*;
import cn.stylefeng.guns.modular.system.service.*;
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
    @Autowired
    private IMonthCountService monthCountService;
    @Autowired
    private IImageChangeService imageChangeService;

    //每天10点
    @Scheduled(cron = "0 0 10 * * ?")
    public void everyDaySendMail() {
        logger.info("everyDaySendMail run");
        sendReportMail("1");
    }

    //每周一11点
    @Scheduled(cron = "0 0 11 ? * MON")
    public void everyWeekSendMail() {
        logger.info("everyWeekSendMail run");
        sendReportMail("2");
    }

    //每月1日12点
    @Scheduled(cron = "0 0 12 1 * ?")
    public void everyMonthSendMail() {
        logger.info("everyMonthSendMail run");
        sendReportMail("3");

    }

    private void sendReportMail(String cycle) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
        int year = yesterday.getYear();
        int month = yesterday.getMonthValue();


        List<Dept> depts = deptService.getDeptBySendEmailCycle(cycle);

        for (Dept d : depts) {
            List<MonthAttendance> ads = monthAttendanceService.getMonthAttendanceByYearMonthDeptId(year, month, d.getId());
            if (ads.size() > 0) {
                String fileName = d.getSimplename() + yesterday.format(dtf) + "月度考勤表.xls";
                File f = new File(fileName);
                if (f.exists()) {
                    f.delete();
                }
                monthAttendanceService.exportMonthAttendanceReportXls(ads, fileName);

                if (f.exists()) {
                    List<String> toEmails = new ArrayList<String>();
                    if (d.getEmail1() != null) {
                        toEmails.add(d.getEmail1());
                    }
                    if (d.getEmail2() != null) {
                        toEmails.add(d.getEmail2());
                    }
                    if (d.getEmail3() != null) {
                        toEmails.add(d.getEmail3());
                    }
                    String[] to = new String[toEmails.size()];
                    for (int i = 0; i != to.length; i++) {
                        to[i] = toEmails.get(i);
                    }
                    PersonUtil.sendMail(mailSender,
                            "18565430729@163.com",
                            to,
                            fileName,
                            "",
                            new File(fileName),
                            yesterday.format(dtf) + ".xls");
                }
            }
        }
    }

    //每天2点跑批 更新月度考勤表 月度统计表
    @Scheduled(cron = "0 0 2 ? * *")
    public void runButch() {
        logger.info("runButch run");

        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
        //更新指定日期的考勤记录备注
        int markRlt = attendanceRecordService.markAttendanceRecord(yesterday);

        //统计指定日期的月度考勤表
        int statisticsRlt = attendanceRecordService.statisticsOneDayAttendRecords(yesterday);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");

        String date = yesterday.format(dtf);

        //统计月度统计表
        monthCountService.deleteMonthCountByDate(date);
        int insertNum = monthCountService.insertNewMonthCountByDate(date);

    }

    //重试影像同步
    @Scheduled(cron = "0 0/5 * * * ?")
    public void retryImageChange() {
        logger.info("retryImageChange run");

        List<ImageChange> ics = imageChangeService.getErrorImageChanges();
        for (ImageChange ic:ics) {
            imageChangeService.changeImage(ic);
        }
    }

    //AttendanceRecord 补充机构信息
    @Scheduled(cron = "0 0/5 * * * ?")
    public void fillAttendRecordDeptInfo() {
        logger.info("fillAttendRecordDeptInfo run");

        int num = attendanceRecordService.fillAttendRecordDeptInfo();

        logger.info("考勤记录补充个人信息执行完毕:"+num);
    }


}
