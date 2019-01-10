package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-10
 */
@TableName("user_attendance_detail")
public class AttendanceDetail extends Model<AttendanceDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;
    private String date;
    @TableField("user_id")
    private String userId;
    private String type;

    public AttendanceDetail() {
    }

    public AttendanceDetail(String id, String date, String userId, String type) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AttendanceDetail{" +
        ", id=" + id +
        ", date=" + date +
        ", userId=" + userId +
        ", type=" + type +
        "}";
    }
}
