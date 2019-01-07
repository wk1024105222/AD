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
import cn.stylefeng.guns.modular.system.model.ImageChange;
import cn.stylefeng.guns.modular.system.service.IImageChangeService;

/**
 * 影像管理控制器
 *
 * @author fengshuonan
 * @Date 2019-01-06 13:53:37
 */
@Controller
@RequestMapping("/imageChange")
public class ImageChangeController extends BaseController {

    private String PREFIX = "/system/imageChange/";

    @Autowired
    private IImageChangeService imageChangeService;

    /**
     * 跳转到影像管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "imageChange.html";
    }

    /**
     * 跳转到添加影像管理
     */
    @RequestMapping("/imageChange_add")
    public String imageChangeAdd() {
        return PREFIX + "imageChange_add.html";
    }

    /**
     * 跳转到修改影像管理
     */
    @RequestMapping("/imageChange_update/{imageChangeId}")
    public String imageChangeUpdate(@PathVariable Integer imageChangeId, Model model) {
        ImageChange imageChange = imageChangeService.selectById(imageChangeId);
        model.addAttribute("item",imageChange);
        LogObjectHolder.me().set(imageChange);
        return PREFIX + "imageChange_edit.html";
    }

    /**
     * 获取影像管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return imageChangeService.selectList(null);
    }

    /**
     * 新增影像管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ImageChange imageChange) {
        imageChangeService.insert(imageChange);
        return SUCCESS_TIP;
    }

    /**
     * 删除影像管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer imageChangeId) {
        imageChangeService.deleteById(imageChangeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改影像管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ImageChange imageChange) {
        imageChangeService.updateById(imageChange);
        return SUCCESS_TIP;
    }

    /**
     * 影像管理详情
     */
    @RequestMapping(value = "/detail/{imageChangeId}")
    @ResponseBody
    public Object detail(@PathVariable("imageChangeId") Integer imageChangeId) {
        return imageChangeService.selectById(imageChangeId);
    }
}
