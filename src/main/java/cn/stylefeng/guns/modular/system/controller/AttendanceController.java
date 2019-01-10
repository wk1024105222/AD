package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.service.IMonthAttendanceService;
import cn.stylefeng.guns.modular.system.warpper.AttendanceWarpper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.service.IAttendanceService;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
    public Object list(String condition) {
//        return attendanceRecordService.selectList(null);
//        String[] to = {"1024105222@qq.com","david_wang@comwave.com.cn"};
//        String filePath = "E:/WeChat Files/shengji310065/Files/51还款测试案例--0109(3).xlsx";
//        PersonUtil.sendMail(mailSender,"18565430729@163.com",to,"发多人","测试邮件内容",new File(filePath), "月度考勤表");

        //触发跑批
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        List<AttendanceRecord> ads =  attendanceRecordService.getOneDayAttendRecords(today);
        if(ads.size()<2) {
            return "";
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


        EntityWrapper<AttendanceRecord> wrapper = new EntityWrapper<AttendanceRecord>();
        wrapper.orderDesc(Collections.singleton("attendance_Time"));
        if(condition != null && !"".equalsIgnoreCase(condition)) {
            wrapper.where("user_Id like '%" + condition+"%' or user_name like '%"+condition+"%'");
        }

        List<AttendanceRecord> records = attendanceRecordService.selectList(wrapper);
        List<Map<String, Object>> mapRecords = new ArrayList<Map<String, Object>>(records.size());
        for (AttendanceRecord a : records) {
            mapRecords.add(PersonUtil.beanToMap(a));
        }

        List<Map<String, Object>> result = new AttendanceWarpper(mapRecords).wrap();
        //return new UserWarpper(cameras).wrap();
        return result ;




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
}
