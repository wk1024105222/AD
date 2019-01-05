package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.Camera;
import cn.stylefeng.guns.modular.system.dao.CameraMapper;
import cn.stylefeng.guns.modular.system.service.ICameraService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备状态表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-02
 */
@Service
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements ICameraService {

    @Override
    public List<Map<String, Object>> selectCameras() {
        return this.baseMapper.selectCameras();
    }
}
