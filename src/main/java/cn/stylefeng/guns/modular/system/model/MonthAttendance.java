package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 月度考勤表
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
@TableName("user_month_attendance")
public class MonthAttendance extends Model<MonthAttendance> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 考勤天数
     */
    private Integer days;
    /**
     * 身份证号或员工编号
     */
    private String userId;
    /**
     * 姓名
     */
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
     * 1
     */
    private String day1;
    /**
     * 2
     */
    private String day2;
    /**
     * 3
     */
    private String day3;
    /**
     * 4
     */
    private String day4;
    /**
     * 5
     */
    private String day5;
    /**
     * 6
     */
    private String day6;
    /**
     * 7
     */
    private String day7;
    /**
     * 8
     */
    private String day8;
    /**
     * 9
     */
    private String day9;
    /**
     * 10
     */
    private String day10;
    /**
     * 11
     */
    private String day11;
    /**
     * 12
     */
    private String day12;
    /**
     * 13
     */
    private String day13;
    /**
     * 14
     */
    private String day14;
    /**
     * 15
     */
    private String day15;
    /**
     * 16
     */
    private String day16;
    /**
     * 17
     */
    private String day17;
    /**
     * 18
     */
    private String day18;
    /**
     * 19
     */
    private String day19;
    /**
     * 20
     */
    private String day20;
    /**
     * 21
     */
    private String day21;
    /**
     * 22
     */
    private String day22;
    /**
     * 23
     */
    private String day23;
    /**
     * 24
     */
    private String day24;
    /**
     * 25
     */
    private String day25;
    /**
     * 26
     */
    private String day26;
    /**
     * 27
     */
    private String day27;
    /**
     * 28
     */
    private String day28;
    /**
     * 29
     */
    private String day29;
    /**
     * 30
     */
    private String day30;
    /**
     * 31
     */
    private String day31;

    public MonthAttendance() {
    }

    public MonthAttendance(String id, Integer year, Integer month, String userId) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
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

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getDay3() {
        return day3;
    }

    public void setDay3(String day3) {
        this.day3 = day3;
    }

    public String getDay4() {
        return day4;
    }

    public void setDay4(String day4) {
        this.day4 = day4;
    }

    public String getDay5() {
        return day5;
    }

    public void setDay5(String day5) {
        this.day5 = day5;
    }

    public String getDay6() {
        return day6;
    }

    public void setDay6(String day6) {
        this.day6 = day6;
    }

    public String getDay7() {
        return day7;
    }

    public void setDay7(String day7) {
        this.day7 = day7;
    }

    public String getDay8() {
        return day8;
    }

    public void setDay8(String day8) {
        this.day8 = day8;
    }

    public String getDay9() {
        return day9;
    }

    public void setDay9(String day9) {
        this.day9 = day9;
    }

    public String getDay10() {
        return day10;
    }

    public void setDay10(String day10) {
        this.day10 = day10;
    }

    public String getDay11() {
        return day11;
    }

    public void setDay11(String day11) {
        this.day11 = day11;
    }

    public String getDay12() {
        return day12;
    }

    public void setDay12(String day12) {
        this.day12 = day12;
    }

    public String getDay13() {
        return day13;
    }

    public void setDay13(String day13) {
        this.day13 = day13;
    }

    public String getDay14() {
        return day14;
    }

    public void setDay14(String day14) {
        this.day14 = day14;
    }

    public String getDay15() {
        return day15;
    }

    public void setDay15(String day15) {
        this.day15 = day15;
    }

    public String getDay16() {
        return day16;
    }

    public void setDay16(String day16) {
        this.day16 = day16;
    }

    public String getDay17() {
        return day17;
    }

    public void setDay17(String day17) {
        this.day17 = day17;
    }

    public String getDay18() {
        return day18;
    }

    public void setDay18(String day18) {
        this.day18 = day18;
    }

    public String getDay19() {
        return day19;
    }

    public void setDay19(String day19) {
        this.day19 = day19;
    }

    public String getDay20() {
        return day20;
    }

    public void setDay20(String day20) {
        this.day20 = day20;
    }

    public String getDay21() {
        return day21;
    }

    public void setDay21(String day21) {
        this.day21 = day21;
    }

    public String getDay22() {
        return day22;
    }

    public void setDay22(String day22) {
        this.day22 = day22;
    }

    public String getDay23() {
        return day23;
    }

    public void setDay23(String day23) {
        this.day23 = day23;
    }

    public String getDay24() {
        return day24;
    }

    public void setDay24(String day24) {
        this.day24 = day24;
    }

    public String getDay25() {
        return day25;
    }

    public void setDay25(String day25) {
        this.day25 = day25;
    }

    public String getDay26() {
        return day26;
    }

    public void setDay26(String day26) {
        this.day26 = day26;
    }

    public String getDay27() {
        return day27;
    }

    public void setDay27(String day27) {
        this.day27 = day27;
    }

    public String getDay28() {
        return day28;
    }

    public void setDay28(String day28) {
        this.day28 = day28;
    }

    public String getDay29() {
        return day29;
    }

    public void setDay29(String day29) {
        this.day29 = day29;
    }

    public String getDay30() {
        return day30;
    }

    public void setDay30(String day30) {
        this.day30 = day30;
    }

    public String getDay31() {
        return day31;
    }

    public void setDay31(String day31) {
        this.day31 = day31;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MonthAttendance{" +
        ", id=" + id +
        ", year=" + year +
        ", month=" + month +
        ", days=" + days +
        ", userId=" + userId +
        ", userName=" + userName +
        ", company=" + company +
        ", department=" + department +
        ", team=" + team +
        ", day1=" + day1 +
        ", day2=" + day2 +
        ", day3=" + day3 +
        ", day4=" + day4 +
        ", day5=" + day5 +
        ", day6=" + day6 +
        ", day7=" + day7 +
        ", day8=" + day8 +
        ", day9=" + day9 +
        ", day10=" + day10 +
        ", day11=" + day11 +
        ", day12=" + day12 +
        ", day13=" + day13 +
        ", day14=" + day14 +
        ", day15=" + day15 +
        ", day16=" + day16 +
        ", day17=" + day17 +
        ", day18=" + day18 +
        ", day19=" + day19 +
        ", day20=" + day20 +
        ", day21=" + day21 +
        ", day22=" + day22 +
        ", day23=" + day23 +
        ", day24=" + day24 +
        ", day25=" + day25 +
        ", day26=" + day26 +
        ", day27=" + day27 +
        ", day28=" + day28 +
        ", day29=" + day29 +
        ", day30=" + day30 +
        ", day31=" + day31 +
        "}";
    }
}
