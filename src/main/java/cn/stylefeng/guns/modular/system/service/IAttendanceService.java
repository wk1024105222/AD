package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import com.baomidou.mybatisplus.service.IService;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 考勤记录表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
public interface IAttendanceService extends IService<AttendanceRecord> {

    List<AttendanceRecord> getOneDayAttendRecords(String date);

    void handleSomeOneAttendRecord(LinkedList<AttendanceRecord> attendanceRecords, LocalDate date);

    int statisticsOneDayAttendRecords(LocalDate date);

    int markAttendanceRecord(LocalDate date);

    List<AttendanceRecord> getLackDeptInfoAttendanceRecord();

    int fillAttendRecordDeptInfo();
}
