package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.modular.system.model.MonthCount;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月度统计表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-10
 */
public interface MonthCountMapper extends BaseMapper<MonthCount> {

    void deleteMonthCountByDate(@Param("date") String date);

    List<Map<String,Object>> getMonthCountByDate(@Param("date") String date);
}
