package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import com.baomidou.mybatisplus.service.IService;

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

    void handleSomeOneAttendRecord(List<AttendanceRecord> attendanceRecords);
}
