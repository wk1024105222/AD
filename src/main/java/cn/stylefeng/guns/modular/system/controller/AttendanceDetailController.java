package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.AttendanceDetail;
import cn.stylefeng.guns.modular.system.service.IAttendanceDetailService;

/**
 * 考勤明细控制器
 *
 * @author fengshuonan
 * @Date 2019-01-10 01:48:56
 */
@Controller
@RequestMapping("/attendanceDetail")
public class AttendanceDetailController extends BaseController {

    private String PREFIX = "/system/attendanceDetail/";

    @Autowired
    private IAttendanceDetailService attendanceDetailService;

    /**
     * 跳转到考勤明细首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "attendanceDetail.html";
    }

    /**
     * 跳转到添加考勤明细
     */
    @RequestMapping("/attendanceDetail_add")
    public String attendanceDetailAdd() {
        return PREFIX + "attendanceDetail_add.html";
    }

    /**
     * 跳转到修改考勤明细
     */
    @RequestMapping("/attendanceDetail_update/{attendanceDetailId}")
    public String attendanceDetailUpdate(@PathVariable Integer attendanceDetailId, Model model) {
        AttendanceDetail attendanceDetail = attendanceDetailService.selectById(attendanceDetailId);
        model.addAttribute("item",attendanceDetail);
        LogObjectHolder.me().set(attendanceDetail);
        return PREFIX + "attendanceDetail_edit.html";
    }

    /**
     * 获取考勤明细列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return attendanceDetailService.selectList(null);
    }

    /**
     * 新增考勤明细
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(AttendanceDetail attendanceDetail) {
        attendanceDetailService.insert(attendanceDetail);
        return SUCCESS_TIP;
    }

    /**
     * 删除考勤明细
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer attendanceDetailId) {
        attendanceDetailService.deleteById(attendanceDetailId);
        return SUCCESS_TIP;
    }

    /**
     * 修改考勤明细
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(AttendanceDetail attendanceDetail) {
        attendanceDetailService.updateById(attendanceDetail);
        return SUCCESS_TIP;
    }

    /**
     * 考勤明细详情
     */
    @RequestMapping(value = "/detail/{attendanceDetailId}")
    @ResponseBody
    public Object detail(@PathVariable("attendanceDetailId") Integer attendanceDetailId) {
        return attendanceDetailService.selectById(attendanceDetailId);
    }
}
