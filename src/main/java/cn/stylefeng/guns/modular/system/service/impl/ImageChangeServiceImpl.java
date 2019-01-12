package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.config.properties.GunsProperties;
import cn.stylefeng.guns.core.util.PersonUtil;
import cn.stylefeng.guns.modular.system.dao.ImageChangeMapper;
import cn.stylefeng.guns.modular.system.model.ImageChange;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.service.IImageChangeService;
import cn.stylefeng.guns.modular.system.service.IUserService;
import cn.stylefeng.guns.modular.system.transfer.UserDto;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 影像管理 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-06
 */
@Service
public class ImageChangeServiceImpl extends ServiceImpl<ImageChangeMapper, ImageChange> implements IImageChangeService {

    @Autowired
    private GunsProperties gunsProperties;

    @Autowired
    private IUserService userService;

    @Override
    public void changeImage(ImageChange imgChange) {
        String action = imgChange.getActionType();
        StringBuffer reqMsg = new StringBuffer().append(imgChange.getActionType()).append("|");

        if (action.equals("1")) {
            //新增
            reqMsg.append(imgChange.getImgName());
        } else {
            //删除
            reqMsg.append(imgChange.getImageId());
        }

        String response = PersonUtil.sendMsg(reqMsg.toString());
        imgChange.setSendFlag("1");
        if (response != null) {
            String[] resArray = response.split("\\|");
            imgChange.setReceiveFlag("1");
            if (imgChange.getActionType().equals("1")) {
                //新增结果
                String rlt = resArray[3];
                imgChange.setChangeResult(resArray[2]);
                if (!"-1".equals(rlt)) {
                    //新增成功
                    imgChange.setImageId(resArray[3]);
                    userService.updateImgId(imgChange.getUserId(), resArray[3]);
                }
            } else {
                //删除结果
                imgChange.setChangeResult(resArray[2]);
                userService.updateImgId(imgChange.getUserId(), null);
            }
        }
        imgChange.setUpdateTime(new Date());
        this.insert(imgChange);
    }

    @Override
    public int addImage(UserDto user) {
        //添加新增操作影像同步记录
        String path = gunsProperties.getFileUploadPath() + user.getAvatar();
        ImageChange img = new ImageChange(path,"1",user.getAccount(),new Date(),"0",null,null,null);
//        this.insert(img);
        this.changeImage(img);

        return 1;
    }

    @Override
    public int deleteImage(Integer userId) {
        User user = userService.selectById(userId);
        ImageChange deleteImg = new ImageChange(gunsProperties.getFileUploadPath() + user.getAvatar(),
                "0",user.getAccount(),new Date(),"0",null,null,user.getImgid());

        this.changeImage(deleteImg);
        return 1;
    }

    @Override
    public int updateImage(User oldUser, UserDto user) {
        ImageChange deleteImg = new ImageChange(gunsProperties.getFileUploadPath() + oldUser.getAvatar(),
                "0",user.getAccount(),new Date(),"0",null,null,oldUser.getImgid());
        this.changeImage(deleteImg);
        ImageChange addImg = new ImageChange(gunsProperties.getFileUploadPath() + user.getAvatar(),
                "1",user.getAccount(),new Date(),"0",null,null,null);
        this.changeImage(addImg);

        return 1;
    }

    @Override
    public List<ImageChange> getErrorImageChanges() {
        return this.baseMapper.getErrorImageChanges();
    }
}
