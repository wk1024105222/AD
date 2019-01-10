package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.MonthCount;
import cn.stylefeng.guns.modular.system.dao.MonthCountMapper;
import cn.stylefeng.guns.modular.system.service.IMonthCountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 月度统计表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-10
 */
@Service
public class MonthCountServiceImpl extends ServiceImpl<MonthCountMapper, MonthCount> implements IMonthCountService {

    @Override
    public void deleteMonthCountByDate(String date) {
        this.baseMapper.deleteMonthCountByDate(date);
    }

    @Override
    public void insertNewMonthCountByDate(String date) {
        List<Map<String,Object>> maps = this.baseMapper.getMonthCountByDate(date);
        for(Map<String,Object> map : maps) {
            MonthCount mc  = new MonthCount();
            mc.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            mc.setMonth(date);
            mc.setUserId((String) map.get("user_id"));
            mc.setType((String) map.get("type"));
            mc.setTimes(((Long) map.get("times")).intValue());
            this.insert(mc);
        }


    }
}
