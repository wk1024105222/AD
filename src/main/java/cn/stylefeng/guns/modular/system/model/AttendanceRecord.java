package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 考勤记录表
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
@TableName("user_attendance_record")
public class AttendanceRecord extends Model<AttendanceRecord> {

    private static final long serialVersionUID = 1L;

    private String uuid;
    /**
     * 登记时间
     */
    private String attendanceTime;
    /**
     * 员工编号
     */
    private String userId;
    /**
     * 员工姓名
     */
    private String userName;
    /**
     * 1:进入 2:离开
     */
    private String action;
    /**
     * 照片存储路径
     */
    private String imgPath;
    /**
     * 公司
     */
    private String company;
    /**
     * 部门
     */
    private String department;
    /**
     * 组
     */
    private String team;
    /**
     * 备注
     */
    private String note;
    /**
     * 识别引擎返回的头像id
     */
    private String imgId;

    /**
     * 进出类型
     */
    private String flag;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
        ", uuid=" + uuid +
        ", attendanceTime=" + attendanceTime +
        ", userId=" + userId +
        ", action=" + action +
        ", imgPath=" + imgPath +
        ", company=" + company +
        ", department=" + department +
        ", team=" + team +
        ", note=" + note +
        "}";
    }
}
