package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.modular.system.model.Camera;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备状态表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-02
 */
public interface CameraMapper extends BaseMapper<Camera> {

    /**
     * 根据条件查询摄像头列表
     */
    List<Map<String, Object>> selectCameras();


}
