package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.model.Dept;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.model.MonthCount;
import cn.stylefeng.guns.modular.system.service.IAttendanceService;
import cn.stylefeng.guns.modular.system.service.IDeptService;
import cn.stylefeng.guns.modular.system.service.IMonthAttendanceService;
import cn.stylefeng.guns.modular.system.service.IMonthCountService;
import cn.stylefeng.guns.modular.system.warpper.AttendanceWarpper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 出入记录控制器
 *
 * @author fengshuonan
 * @Date 2019-01-05 17:18:43
 */
@Controller
@RequestMapping("/attendanceRecord")
public class AttendanceController extends BaseController {

    private String PREFIX = "/attendance/attendanceRecord/";

    @Autowired
    private IAttendanceService attendanceRecordService;

    @Autowired
    private IMonthAttendanceService monthAttendanceService;
//
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IMonthCountService monthCountService;


    /**
     * 跳转到出入记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "attendanceRecord.html";
    }

    /**
     * 跳转到添加出入记录
     */
    @RequestMapping("/attendanceRecord_add")
    public String attendanceRecordAdd() {
        return PREFIX + "attendanceRecord_add.html";
    }

    /**
     * 跳转到修改出入记录
     */
    @RequestMapping("/attendanceRecord_update/{attendanceRecordId}")
    public String attendanceRecordUpdate(@PathVariable String attendanceRecordId, Model model) {
        AttendanceRecord attendanceRecord = attendanceRecordService.selectById(attendanceRecordId);
        model.addAttribute("item",attendanceRecord);
        LogObjectHolder.me().set(attendanceRecord);
        return PREFIX + "attendanceRecord_edit.html";
    }

    /**
     * 获取出入记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String user, String date) {
        Integer deptId = ShiroKit.getUser().getDeptId();

        List<AttendanceRecord> records = attendanceRecordService.getList(user, date, deptId);

        List<Map<String, Object>> mapRecords = new ArrayList<Map<String, Object>>(records.size());
        for (AttendanceRecord a : records) {
            mapRecords.add(PersonUtil.beanToMap(a));
        }

        List<Map<String, Object>> result = new AttendanceWarpper(mapRecords).wrap();
        return result;

    }

    /**
     * 新增出入记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(AttendanceRecord attendanceRecord) {
        attendanceRecordService.insert(attendanceRecord);
        return SUCCESS_TIP;
    }

    /**
     * 删除出入记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer attendanceRecordId) {
        attendanceRecordService.deleteById(attendanceRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改出入记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(AttendanceRecord attendanceRecord) {
        attendanceRecordService.updateById(attendanceRecord);
        return SUCCESS_TIP;
    }

    /**
     * 出入记录详情
     */
    @RequestMapping(value = "/detail/{attendanceRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("attendanceRecordId") Integer attendanceRecordId) {
        return attendanceRecordService.selectById(attendanceRecordId);
    }

    @RequestMapping(value = "/byhand/{date}")
    @ResponseBody
    public String byhand(@PathVariable("date") String date) {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate yesterday = LocalDate.parse(date,dtf1);

        //更新指定日期的考勤记录备注
        int markRlt = attendanceRecordService.markAttendanceRecord(yesterday);

        //统计指定日期的月度考勤表
        int statisticsRlt = attendanceRecordService.statisticsOneDayAttendRecords(yesterday);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");

        String yearMonth = yesterday.format(dtf);

        //统计月度统计表
        monthCountService.deleteMonthCountByDate(yearMonth);
        int insertNum = monthCountService.insertNewMonthCountByDate(yearMonth);

        return "ok"+new Date();
    }

    @RequestMapping("/exportMA/{date}")
    @ResponseBody
    public String exportMA(@PathVariable("date") String date) {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate yesterday = LocalDate.parse(date, dtf1).minus(1, ChronoUnit.DAYS);

        int year = yesterday.getYear();
        int month = yesterday.getMonthValue();


        List<Dept> depts = deptService.getDeptBySendEmailCycle("1");
        StringBuffer result = new StringBuffer();

        for (Dept d : depts) {
            File file = null;
            HSSFWorkbook workbook = new HSSFWorkbook();

            List<MonthAttendance> ads = monthAttendanceService.getMonthAttendanceByYearMonthDeptId(year, month, d.getId());
            if (ads.size() > 0) {
                monthAttendanceService.exportMonthAttendanceReportXls(workbook,ads);
            }
            List<MonthCount> records = monthCountService.list(null,yesterday.format(dtf2), null,d.getId());
            if(records.size()>0) {
                monthCountService.exportMonthCountReportXls(workbook,records);
            }

            String fileName = d.getSimplename()+"_" + yesterday.format(dtf2) + "_月度考勤表.xls";
            result.append(fileName).append(",");

            file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                workbook.write(fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return result.toString();
    }
}
