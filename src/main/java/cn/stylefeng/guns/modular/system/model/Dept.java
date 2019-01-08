package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
@TableName("sys_dept")
public class Dept extends Model<Dept> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 排序
     */
    private Integer num;
    /**
     * 父部门id
     */
    private Integer pid;
    /**
     * 父级ids
     */
    private String pids;
    /**
     * 简称
     */
    private String simplename;
    /**
     * 全称
     */
    private String fullname;
    /**
     * 提示
     */
    private String tips;
    /**
     * 版本（乐观锁保留字段）
     */
    private Integer version;
    /**
     * 每日打卡次数
     */
    @TableField("attend_times")
    private Integer attendTimes;
    /**
     * 上班时间
     */
    @TableField("start_work_time")
    private String startWorkTime;
    /**
     * 下班时间
     */
    @TableField("end_work_time")
    private String endWorkTime;
    /**
     * 午休开始时间
     */
    @TableField("start_rest_time")
    private String startRestTime;
    /**
     * 午休结束
     */
    @TableField("end_rest_time")
    private String endRestTime;
    /**
     * 离岗时间
     */
    @TableField("leave_time")
    private Integer leaveTime;
    /**
     * 加班开始时间
     */
    @TableField("start_over_time")
    private String startOverTime;
    private String email1;
    private String email2;
    private String email3;
    /**
     * 报告发送周期 1:1天 2:1星期 3:1年
     */
    @TableField("send_email_cycle")
    private String sendEmailCycle;
    /**
     * 工作日,分隔
     */
    @TableField("work_day")
    private String workDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getSimplename() {
        return simplename;
    }

    public void setSimplename(String simplename) {
        this.simplename = simplename;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getAttendTimes() {
        return attendTimes;
    }

    public void setAttendTimes(Integer attendTimes) {
        this.attendTimes = attendTimes;
    }

    public String getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(String startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public String getEndWorkTime() {
        return endWorkTime;
    }

    public void setEndWorkTime(String endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    public String getStartRestTime() {
        return startRestTime;
    }

    public void setStartRestTime(String startRestTime) {
        this.startRestTime = startRestTime;
    }

    public String getEndRestTime() {
        return endRestTime;
    }

    public void setEndRestTime(String endRestTime) {
        this.endRestTime = endRestTime;
    }

    public String getStartOverTime() {
        return startOverTime;
    }

    public void setStartOverTime(String startOverTime) {
        this.startOverTime = startOverTime;
    }

    public Integer getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Integer leaveTime) {
        this.leaveTime = leaveTime;
    }


    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getSendEmailCycle() {
        return sendEmailCycle;
    }

    public void setSendEmailCycle(String sendEmailCycle) {
        this.sendEmailCycle = sendEmailCycle;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Dept{" +
                ", id=" + id +
                ", num=" + num +
                ", pid=" + pid +
                ", pids=" + pids +
                ", simplename=" + simplename +
                ", fullname=" + fullname +
                ", tips=" + tips +
                ", version=" + version +
                ", attendTimes=" + attendTimes +
                ", startWorkTime=" + startWorkTime +
                ", endWorkTime=" + endWorkTime +
                ", startRestTime=" + startRestTime +
                ", endRestTime=" + endRestTime +
                ", leaveTime=" + leaveTime +
                ", startOverTime=" + startOverTime +
                ", email1=" + email1 +
                ", email2=" + email2 +
                ", email3=" + email3 +
                ", sendEmailCycle=" + sendEmailCycle +
                ", workDay=" + workDay +
                "}";
    }
}
