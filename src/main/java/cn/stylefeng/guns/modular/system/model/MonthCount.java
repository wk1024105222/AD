package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 月度统计表
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-10
 */
@TableName("user_month_count")
public class MonthCount extends Model<MonthCount> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;
    /**
     * 月份
     */
    private String month;
    /**
     * 工号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 姓名
     */
    @TableField("user_name")
    private String userName;
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
     * 次数
     */
    private Integer times;
    /**
     * 类型
     */
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
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
        return "MonthCount{" +
        ", id=" + id +
        ", month=" + month +
        ", userId=" + userId +
        ", userName=" + userName +
        ", company=" + company +
        ", department=" + department +
        ", team=" + team +
        ", times=" + times +
        ", type=" + type +
        "}";
    }
}
