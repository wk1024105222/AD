package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.warpper.AttendanceWarpper;
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
import cn.stylefeng.guns.modular.system.model.MonthCount;
import cn.stylefeng.guns.modular.system.service.IMonthCountService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 月度统计表控制器
 *
 * @author fengshuonan
 * @Date 2019-01-10 01:47:31
 */
@Controller
@RequestMapping("/monthCount")
public class MonthCountController extends BaseController {

    private String PREFIX = "/system/monthCount/";

    @Autowired
    private IMonthCountService monthCountService;

    /**
     * 跳转到月度统计表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "monthCount.html";
    }

    /**
     * 跳转到添加月度统计表
     */
    @RequestMapping("/monthCount_add")
    public String monthCountAdd() {
        return PREFIX + "monthCount_add.html";
    }

    /**
     * 跳转到修改月度统计表
     */
    @RequestMapping("/monthCount_update/{monthCountId}")
    public String monthCountUpdate(@PathVariable Integer monthCountId, Model model) {
        MonthCount monthCount = monthCountService.selectById(monthCountId);
        model.addAttribute("item",monthCount);
        LogObjectHolder.me().set(monthCount);
        return PREFIX + "monthCount_edit.html";
    }

    /**
     * 获取月度统计表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String user, String month) {

        EntityWrapper<MonthCount> wrapper = new EntityWrapper<MonthCount>();
        wrapper.where("1=1");
        if(user != null && !"".equalsIgnoreCase(user)) {
            wrapper.where("(user_Id like '%" + user+"%' or user_name like '%"+user+"%')");
        }
        if(month != null && !"".equalsIgnoreCase(month)) {
            wrapper.eq("month",month);
        }
        wrapper.orderDesc(Collections.singleton("month")).orderDesc(Collections.singleton("user_Id"));

        List<MonthCount> records = monthCountService.selectList(wrapper);
        return records ;

//        return monthCountService.selectList(null);
    }

    /**
     * 新增月度统计表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(MonthCount monthCount) {
        monthCountService.insert(monthCount);
        return SUCCESS_TIP;
    }

    /**
     * 删除月度统计表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer monthCountId) {
        monthCountService.deleteById(monthCountId);
        return SUCCESS_TIP;
    }

    /**
     * 修改月度统计表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(MonthCount monthCount) {
        monthCountService.updateById(monthCount);
        return SUCCESS_TIP;
    }

    /**
     * 月度统计表详情
     */
    @RequestMapping(value = "/detail/{monthCountId}")
    @ResponseBody
    public Object detail(@PathVariable("monthCountId") Integer monthCountId) {
        return monthCountService.selectById(monthCountId);
    }
}
