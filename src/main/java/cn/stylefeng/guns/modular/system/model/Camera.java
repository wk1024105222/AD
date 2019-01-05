package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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

    private String uuid;
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


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Camera{" +
        ", uuid=" + uuid +
        ", cameraId=" + cameraId +
        ", address=" + address +
        ", status=" + status +
        "}";
    }
}
