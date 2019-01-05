package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.dao.AttendanceRecordMapper;
import cn.stylefeng.guns.modular.system.service.IAttendanceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考勤记录表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements IAttendanceService {

}
