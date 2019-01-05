package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.Camera;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备状态表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-02
 */
public interface ICameraService extends IService<Camera> {

    List<Map<String,Object>> selectCameras();
}
