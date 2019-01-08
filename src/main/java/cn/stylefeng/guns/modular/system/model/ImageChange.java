package cn.stylefeng.guns.modular.system.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 影像管理
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-06
 */
@TableName("user_image_change")
public class ImageChange extends Model<ImageChange> {

    private static final long serialVersionUID = 1L;

    /**
     * 照片filename
     */
    @TableId("img_name")
    private String imgName;
    /**
     * 0：删除 1：新增
     */
    @TableField("action_type")
    private String actionType;
    /**
     * 工号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 创建时间yyyy-MM-dd HH:mm:ss
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 发送状态 null/0：未发送 1：已发送 2：发送失败
     */
    @TableField("send_flag")
    private String sendFlag;
    /**
     * 返回状态 null：未返回 0：未返回 1：成功 2：失败
     */
    @TableField("receive_flag")
    private String receiveFlag;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 人脸识别引擎返回的照片id
     */
    @TableField("image_id")
    private String imageId;
    /**
     * 人脸识别引擎返回的结果
     */
    @TableField("change_result")
    private String changeResult;

    public ImageChange(String imgName, String actionType, String userId, Date createTime, String sendFlag, String receiveFlag, Date updateTime, String imageId) {
        this.imgName = imgName;
        this.actionType = actionType;
        this.userId = userId;
        this.createTime = createTime;
        this.sendFlag = sendFlag;
        this.receiveFlag = receiveFlag;
        this.updateTime = updateTime;
        this.imageId = imageId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getReceiveFlag() {
        return receiveFlag;
    }

    public void setReceiveFlag(String receiveFlag) {
        this.receiveFlag = receiveFlag;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getChangeResult() {
        return changeResult;
    }

    public void setChangeResult(String changeResult) {
        this.changeResult = changeResult;
    }

    @Override
    protected Serializable pkVal() {
        return this.imgName;
    }

    @Override
    public String toString() {
        return "ImageChange{" +
        ", imgName=" + imgName +
        ", actionType=" + actionType +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", sendFlag=" + sendFlag +
        ", receiveFlag=" + receiveFlag +
        ", updateTime=" + updateTime +
        ", imageId=" + imageId +
        "}";
    }
}
