package cn.stylefeng.guns.modular.system.service;

import cn.stylefeng.guns.modular.system.model.MonthCount;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 月度统计表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-10
 */
public interface IMonthCountService extends IService<MonthCount> {

    void deleteMonthCountByDate(String date);

    int insertNewMonthCountByDate(String date);
}
