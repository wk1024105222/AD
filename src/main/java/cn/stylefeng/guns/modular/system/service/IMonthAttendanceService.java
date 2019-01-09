package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 月度考勤表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
public interface IMonthAttendanceService extends IService<MonthAttendance> {

    MonthAttendance getMonthAttendanceByYearMonthUserId(String year, String month, String userId);
}
