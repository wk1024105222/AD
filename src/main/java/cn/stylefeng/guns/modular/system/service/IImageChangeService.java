package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.ImageChange;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.transfer.UserDto;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 影像管理 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-06
 */
public interface IImageChangeService extends IService<ImageChange> {

    int addImage(UserDto user);

    int deleteImage(Integer userId);

    int updateImage(User oldUser, UserDto user);
}
