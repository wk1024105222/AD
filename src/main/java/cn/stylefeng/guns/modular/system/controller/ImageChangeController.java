package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.model.Camera;
import cn.stylefeng.guns.modular.system.warpper.CameraWarpper;
import cn.stylefeng.guns.modular.system.warpper.ImageChangeWarpper;
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
import cn.stylefeng.guns.modular.system.model.ImageChange;
import cn.stylefeng.guns.modular.system.service.IImageChangeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
//        return imageChangeService.selectList(null);

        EntityWrapper<ImageChange> wrapper = new EntityWrapper<ImageChange>();
        wrapper.orderDesc(Collections.singleton("create_time"));
        if(condition != null && !"".equalsIgnoreCase(condition)) {
            wrapper.where("user_id = " + condition);
        }

        List<ImageChange> imageChanges = imageChangeService.selectList(wrapper);
        List<Map<String, Object>> imageChangesMap = new ArrayList<Map<String, Object>>(imageChanges.size());
        for (ImageChange i : imageChanges) {
            imageChangesMap.add(PersonUtil.beanToMap(i));
        }

        List<Map<String, Object>> result = new ImageChangeWarpper(imageChangesMap).wrap();
        //return new UserWarpper(cameras).wrap();
        return result ;
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
