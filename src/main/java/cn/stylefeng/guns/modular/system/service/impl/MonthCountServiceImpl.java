package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.Dept;
import cn.stylefeng.guns.modular.system.model.MonthCount;
import cn.stylefeng.guns.modular.system.dao.MonthCountMapper;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.service.IDeptService;
import cn.stylefeng.guns.modular.system.service.IMonthCountService;
import cn.stylefeng.guns.modular.system.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Autowired
    private IDeptService deptService;
    @Autowired
    private IUserService userService;

    @Override
    public void deleteMonthCountByDate(String date) {
        this.baseMapper.deleteMonthCountByDate(date);
    }

    @Override
    public int insertNewMonthCountByDate(String date) {
        List<Dept> depts = deptService.selectList(null);
        Map<String,Dept> idToDept = new HashMap<String,Dept>();

        for (Dept d : depts) {
            idToDept.put(d.getId().toString(),d);
        }

        List<User> users = userService.selectList(null);
        Map<String,User> idToUser = new HashMap<String,User>();

        for (User u : users) {
            idToUser.put(u.getAccount(),u);
        }

        List<Map<String,Object>> maps = this.baseMapper.getMonthCountByDate(date);
        User user = null;
        Dept dept = null;
        String pids = null;
        int rank = 0;
        for(Map<String,Object> map : maps) {
            MonthCount mc  = new MonthCount();
            mc.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            mc.setMonth(date);
            mc.setUserId((String) map.get("user_id"));
            mc.setType((String) map.get("type"));
            mc.setTimes(((Long) map.get("times")).intValue());
            mc.setDates((String) map.get("dates"));

            user = idToUser.get(map.get("user_id"));
            dept = idToDept.get(user.getDeptid().toString());

            mc.setUserName(idToUser.get(map.get("user_id")).getName());

            pids = dept.getPids();
            rank = pids.split(",").length;
            if(rank == 3) {
                int index1=pids.indexOf(',');
                int index2=pids.indexOf(',',index1+1);
                mc.setCompany(idToDept.get(pids.substring(index1+2,index2-1)).getSimplename());
                mc.setDepartment(idToDept.get(pids.substring(index2+2,pids.length()-2)).getSimplename());
                mc.setTeam(idToDept.get(user.getDeptid().toString()).getSimplename());

            } else if(rank == 2) {
                int index1=pids.indexOf(',');
                mc.setCompany(idToDept.get(pids.substring(index1+2,pids.length()-2)).getSimplename());
                mc.setDepartment(idToDept.get(user.getDeptid().toString()).getSimplename());
            } else {
                mc.setCompany(idToDept.get(user.getDeptid().toString()).getSimplename());
            }

            this.insert(mc);
        }

        return maps.size();
    }

    @Override
    public List<MonthCount> list(String user, String month, Integer deptId) {
        return this.baseMapper.list(user,month,deptId);
    }
}
