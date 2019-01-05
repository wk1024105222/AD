package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.util.Contrast;
import cn.stylefeng.guns.modular.system.model.Camera;
import cn.stylefeng.guns.modular.system.warpper.AttendanceWarpper;
import cn.stylefeng.guns.modular.system.warpper.CameraWarpper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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


        EntityWrapper<AttendanceRecord> wrapper = new EntityWrapper<AttendanceRecord>();
        wrapper.orderDesc(Collections.singleton("attendance_Time"));
        if(condition != null && !"".equalsIgnoreCase(condition)) {
            wrapper.where("user_Id like '%" + condition+"%' or user_name like '%"+condition+"%'");
        }

        List<AttendanceRecord> records = attendanceRecordService.selectList(wrapper);
        List<Map<String, Object>> mapRecords = new ArrayList<Map<String, Object>>(records.size());
        for (AttendanceRecord a : records) {
            mapRecords.add(Contrast.beanToMap(a));
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
