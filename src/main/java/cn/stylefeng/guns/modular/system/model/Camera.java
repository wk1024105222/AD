package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 设备状态表
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-02
 */
@TableName("user_camera")
public class Camera extends Model<Camera> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;
    /**
     * 设备编号
     */
    @TableField("camera_Id")
    private String cameraId;
    /**
     * 地址
     */
    private String address;
    /**
     * 1:正常 2:故障
     */
    private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Camera{" +
        ", id=" + id +
        ", cameraId=" + cameraId +
        ", address=" + address +
        ", status=" + status +
        "}";
    }
}
