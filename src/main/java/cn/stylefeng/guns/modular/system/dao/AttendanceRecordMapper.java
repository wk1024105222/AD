package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {

    List<AttendanceRecord> getOneDayAttendRecords(@Param("date") String date);

    List<Map<String,Object>> statisticsOneDayAttendRecords(@Param("date") String date);

    List<AttendanceRecord> getLackDeptInfoAttendanceRecord();
}
