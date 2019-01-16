package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.model.MonthCount;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.service.IMonthAttendanceService;

import java.util.Collections;
import java.util.List;

/**
 * 月度考勤表控制器
 *
 * @author fengshuonan
 * @Date 2019-01-05 21:19:50
 */
@Controller
@RequestMapping("/monthAttendance")
public class MonthAttendanceController extends BaseController {

    private String PREFIX = "/system/monthAttendance/";

    @Autowired
    private IMonthAttendanceService monthAttendanceService;

    /**
     * 跳转到月度考勤表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "monthAttendance.html";
    }

    /**
     * 跳转到添加月度考勤表
     */
    @RequestMapping("/monthAttendance_add")
    public String monthAttendanceAdd() {
        return PREFIX + "monthAttendance_add.html";
    }

    /**
     * 跳转到修改月度考勤表
     */
    @RequestMapping("/monthAttendance_update/{monthAttendanceId}")
    public String monthAttendanceUpdate(@PathVariable Integer monthAttendanceId, Model model) {
        MonthAttendance monthAttendance = monthAttendanceService.selectById(monthAttendanceId);
        model.addAttribute("item",monthAttendance);
        LogObjectHolder.me().set(monthAttendance);
        return PREFIX + "monthAttendance_edit.html";
    }

    /**
     * 获取月度考勤表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String user, String year,String month) {

        Integer deptId = ShiroKit.getUser().getDeptId();

        return monthAttendanceService.list(user,
                                            year==null||year.equals("")?null:Integer.parseInt(year),
                                            month==null||month.equals("")?null:Integer.parseInt(month),
                                            deptId);
    }

    /**
     * 新增月度考勤表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(MonthAttendance monthAttendance) {
        monthAttendanceService.insert(monthAttendance);
        return SUCCESS_TIP;
    }

    /**
     * 删除月度考勤表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer monthAttendanceId) {
        monthAttendanceService.deleteById(monthAttendanceId);
        return SUCCESS_TIP;
    }

    /**
     * 修改月度考勤表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(MonthAttendance monthAttendance) {
        monthAttendanceService.updateById(monthAttendance);
        return SUCCESS_TIP;
    }

    /**
     * 月度考勤表详情
     */
    @RequestMapping(value = "/detail/{monthAttendanceId}")
    @ResponseBody
    public Object detail(@PathVariable("monthAttendanceId") Integer monthAttendanceId) {
        return monthAttendanceService.selectById(monthAttendanceId);
    }
}
