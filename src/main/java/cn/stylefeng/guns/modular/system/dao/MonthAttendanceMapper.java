package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 月度考勤表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
public interface MonthAttendanceMapper extends BaseMapper<MonthAttendance> {

    MonthAttendance getMonthAttendanceByYearMonthUserId(@Param("year") Integer year,
                                                        @Param("month") Integer month,
                                                        @Param("userId") String userId);

    List<MonthAttendance> getMonthAttendanceByYearMonthDeptId(@Param("year") Integer year,
                                                              @Param("month") Integer month,
                                                             @Param("deptId") Integer deptId);
}
