package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.ImageChange;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.transfer.UserDto;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 影像管理 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-06
 */
public interface IImageChangeService extends IService<ImageChange> {

    Boolean addImage(User user);

    Boolean deleteImage(Integer userId);

    Boolean updateImage(User oldUser, UserDto user);

    List<ImageChange> getErrorImageChanges();

    Boolean changeImage(ImageChange imgChange) ;
}
