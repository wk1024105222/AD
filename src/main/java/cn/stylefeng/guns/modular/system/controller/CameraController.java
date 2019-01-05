package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.util.Contrast;
import cn.stylefeng.guns.modular.system.warpper.CameraWarpper;
import cn.stylefeng.guns.modular.system.warpper.UserWarpper;
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
import cn.stylefeng.guns.modular.system.model.Camera;
import cn.stylefeng.guns.modular.system.service.ICameraService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 设备监控控制器
 *
 * @author fengshuonan
 * @Date 2019-01-02 16:24:22
 */
@Controller
@RequestMapping("/camera")
public class CameraController extends BaseController {

    private String PREFIX = "/system/camera/";

    @Autowired
    private ICameraService cameraService;

    /**
     * 跳转到设备监控首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "camera.html";
    }

    /**
     * 跳转到添加设备监控
     */
    @RequestMapping("/camera_add")
    public String cameraAdd() {
        return PREFIX + "camera_add.html";
    }

    /**
     * 跳转到修改设备监控
     */
    @RequestMapping("/camera_update/{cameraId}")
    public String cameraUpdate(@PathVariable Integer cameraId, Model model) {
        Camera camera = cameraService.selectById(cameraId);
        model.addAttribute("item",camera);
        LogObjectHolder.me().set(camera);
        return PREFIX + "camera_edit.html";
    }

    /**
     * 获取设备监控列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
//        List<Map<String, Object>> cameras = cameraService.selectCameras();
        EntityWrapper<Camera> wrapper = new EntityWrapper<Camera>();
        wrapper.orderDesc(Collections.singleton("camera_Id"));
        if(condition != null && !"".equalsIgnoreCase(condition)) {
            wrapper.where("camera_Id = " + condition);
        }

        List<Camera> cameras = cameraService.selectList(wrapper);
        List<Map<String, Object>> cameras1 = new ArrayList<Map<String, Object>>(cameras.size());
        for (Camera c : cameras) {
            cameras1.add(Contrast.beanToMap(c));
        }

        List<Map<String, Object>> result = new CameraWarpper(cameras1).wrap();
        //return new UserWarpper(cameras).wrap();
        return result ;
    }

    /**
     * 新增设备监控
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Camera camera) {
        cameraService.insert(camera);
        return SUCCESS_TIP;
    }

    /**
     * 删除设备监控
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cameraId) {
        cameraService.deleteById(cameraId);
        return SUCCESS_TIP;
    }

    /**
     * 修改设备监控
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Camera camera) {
        cameraService.updateById(camera);
        return SUCCESS_TIP;
    }

    /**
     * 设备监控详情
     */
    @RequestMapping(value = "/detail/{cameraId}")
    @ResponseBody
    public Object detail(@PathVariable("cameraId") Integer cameraId) {
        return cameraService.selectById(cameraId);
    }
}
