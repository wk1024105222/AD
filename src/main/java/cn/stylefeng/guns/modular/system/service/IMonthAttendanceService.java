package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import com.baomidou.mybatisplus.service.IService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * <p>
 * 月度考勤表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
public interface IMonthAttendanceService extends IService<MonthAttendance> {

    List<MonthAttendance> getMonthAttendanceByYearMonthUserId(Integer year, Integer month, String userId);

    List<MonthAttendance> getMonthAttendanceByYearMonthDeptId(Integer year, Integer month, Integer id);

    boolean exportMonthAttendanceReportXls(HSSFWorkbook workbook, List<MonthAttendance> ads);

    List<MonthAttendance> list(String user, Integer year, Integer month, Integer deptId);
}
