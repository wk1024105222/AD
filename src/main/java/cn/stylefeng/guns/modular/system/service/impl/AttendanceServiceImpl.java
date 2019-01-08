package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.dao.AttendanceRecordMapper;
import cn.stylefeng.guns.modular.system.model.Dept;
import cn.stylefeng.guns.modular.system.service.IAttendanceService;
import cn.stylefeng.guns.modular.system.service.IDeptService;
import cn.stylefeng.guns.modular.system.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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


    @Autowired
    private IDeptService deptService;

    @Override
    public List<AttendanceRecord> getOneDayAttendRecords(String date) {

        return this.baseMapper.getOneDayAttendRecords(date);
    }


//    public void handleSomeOneAttendRecord1(List<AttendanceRecord> adrs) {
//        List<OneEnterOneLeave> oeols = new ArrayList<OneEnterOneLeave>();
//        String startWorkId = null;
//        //将一天的打卡记录分成N对 1对是一进一出
//        //找到第一条进入的记录下标
//        for(int i=0; i!=adrs.size();  ) {
//            String firstAction =  adrs.get(i-1).getAction();
//            String secondAction = adrs.get(i).getAction();
//            if(secondAction.equals(firstAction)) {
//                if(firstAction.equals("1")) {
//                    oeols.add(new OneEnterOneLeave(adrs.get(i-1),null));
//                    if(startWorkId == null) {
//                        startWorkId = adrs.get(i-1).getUuid();
//                    }
//                    i++;
//                } else {
//                    oeols.add(new OneEnterOneLeave(null,adrs.get(i-1)));
//                    oeols.add(new OneEnterOneLeave(null,adrs.get(i)));
//                    i+=2;
//                }
//            } else {
//                if(firstAction.equals("1")) {
//                    oeols.add(new OneEnterOneLeave(adrs.get(i-1),adrs.get(i)));
//                    if(startWorkId == null) {
//                        startWorkId = adrs.get(i-1).getUuid();
//                    }
//                    i+=2;
//                } else {
//                    oeols.add(new OneEnterOneLeave(null,adrs.get(i-1)));
//                    i++;
//                }
//            }
//        }
//
//        Dept dept = deptService.getDeptByUserId(adrs.get(0).getUserId());
//        String startWorkTime = dept.getStartWorkTime();
//        String endWorkTime = dept.getEndWorkTime();
//        String startRestTime = dept.getStartRestTime();
//        String endRestTime = dept.getEndRestTime();
//        int leaveTime = dept.getLeaveTime();
//        String startOverTime = dept.getStartOverTime();
//        String workDay = dept.getWorkDay();
//
//        for(OneEnterOneLeave o : oeols) {
//            if(o.isComplete()) {
//                markEnterAndLeave(o, dept, startWorkId);
//            } else {
//
//            }
//        }
//    }

    @Override
    public void handleSomeOneAttendRecord(List<AttendanceRecord> adrs) {
        Dept dept = deptService.getDeptByUserId(adrs.get(0).getUserId());

        List<OneLeaveOneEnter> oeols = new ArrayList<OneLeaveOneEnter>();
        String startWorkId = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        int i;
        //找到第一条进入记录 作为上班考勤记录
        for (i = 0; i != adrs.size(); i++) {
            if (adrs.get(i).getAction().equals("1")) {
                startWorkId = adrs.get(i).getUuid();
                String attendTime = adrs.get(i).getAttendanceTime().substring(12);
                if (attendTime.compareTo(dept.getStartWorkTime()) <= 0) {
                    adrs.get(i).setFlag("SI");
                    adrs.get(i).setNote("正常上班");
                } else {
                    adrs.get(i).setFlag("FI");
                    try {
                        int m = (int) Math.ceil((sdf.parse(attendTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                        adrs.get(i).setNote("上班迟到" + m + "分钟");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            } else {
                adrs.get(i).setFlag("MO");
                adrs.get(i).setNote("上班前的外出");
            }
        }

        if ((adrs.size() - 1) - i == 0) {
            //仅有一条上班考勤记录
            adrs.get(i).setFlag("FI");
            adrs.get(i).setNote("缺少下班考勤记录");
        } else if ((adrs.size() - 1) - i == 1) {
            //上班后还有一次考勤记录
            AttendanceRecord ad = adrs.get(adrs.size() - 1);
            if (ad.getAction().equals("2")) {
                //上班后仅有一次离开 算下班
                if (ad.getAttendanceTime().compareTo(dept.getStartOverTime()) > 0) {
                    //加班
                    ad.setFlag("FO");
                    try {
                        int m = (int) Math.ceil((sdf.parse(ad.getAttendanceTime()).getTime() - sdf.parse(dept.getStartOverTime()).getTime()) / (1000 * 60));
                        adrs.get(i).setNote("加班" + m + "分钟");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (ad.getAttendanceTime().compareTo(dept.getEndWorkTime()) >= 0) {
                    //正常下班
                    adrs.get(i).setFlag("SO");
                    adrs.get(i).setNote("正常下班");
                } else {
                    //早退
                    ad.setFlag("FO");
                    try {
                        int m = (int) Math.ceil((sdf.parse(dept.getEndWorkTime()).getTime() - sdf.parse(ad.getAttendanceTime()).getTime()) / (1000 * 60));
                        adrs.get(i).setNote("下班早退" + m + "分钟");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //上班有又有一次进入考勤
                adrs.get(i).setFlag("FI");
                adrs.get(i).setNote("缺少下班考勤记录");
            }
        } else {
            //分析上班进入后的数据
            //将剩余打卡记录分成N对 1对是一出一进
            for (int j = i + 2; j != adrs.size(); ) {
                String firstAction = adrs.get(i - 1).getAction();
                String secondAction = adrs.get(i).getAction();
                if (secondAction.equals(firstAction)) {
                    if (firstAction.equals("1")) {
                        //进入进入
                        oeols.add(new OneLeaveOneEnter(null, adrs.get(i - 1)));
                        oeols.add(new OneLeaveOneEnter(null, adrs.get(i)));
                        if (startWorkId == null) {
                            startWorkId = adrs.get(i - 1).getUuid();
                        }
                        i += 2;
                    } else {
                        //离开离开
                        oeols.add(new OneLeaveOneEnter(adrs.get(i - 1), null));
                        i += 1;
                    }
                } else {
                    if (firstAction.equals("1")) {
                        //进入离开
                        oeols.add(new OneLeaveOneEnter(null, adrs.get(i - 1)));
                        if (startWorkId == null) {
                            startWorkId = adrs.get(i - 1).getUuid();
                        }
                        i += 1;
                    } else {
                        //离开进入
                        oeols.add(new OneLeaveOneEnter(adrs.get(i - 1), adrs.get(i)));
                        if (startWorkId == null) {
                            startWorkId = adrs.get(i).getUuid();
                        }
                        i += 2;
                    }
                }
            }
        }

        for (OneLeaveOneEnter o : oeols) {
            if (o.isComplete()) {
                markEnterAndLeave(o, dept, startWorkId);
            } else {

            }
        }
    }

    private void markEnterAndLeave(OneLeaveOneEnter o, Dept dept, String startWorkId) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String[] times = {"00:00:00", dept.getStartWorkTime(), dept.getStartRestTime(),
                dept.getEndRestTime(), dept.getEndWorkTime(), dept.getStartOverTime(), "24:00:00"};

        AttendanceRecord enter = o.enter;
        AttendanceRecord leave = o.leave;

        String enterTime = enter.getAttendanceTime().substring(12);
        String leaveTime = leave.getAttendanceTime().substring(12);

        StringBuffer type = new StringBuffer();

        for (int i = 0; i != times.length - 1; i++) {
            int count = 0;
            if (enterTime.compareTo(times[i]) > 0 && enterTime.compareTo(times[i + 1]) <= 0) {
                type.append("1");
                count += 1;
            }
            if (leaveTime.compareTo(times[i]) > 0 && leaveTime.compareTo(times[i + 1]) <= 0) {
                type.append("1");
                count += 1;
            }
            if (count > 0) {
                type.append("0");
            }
        }
        switch (type.toString()) {

            case "1100000":
                //上班前一次出入
                leave.setFlag("MO");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                enter.setFlag("MI");//正常进入
                enter.setNote("未到上班时间进入");
                break;
            case "1010000":
                leave.setFlag("MO");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("FI");
                        enter.setNote("离岗超时");
                    } else {
                        enter.setFlag("SI");
                    }
//                    enter.setNote("离岗"+m+"分钟");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "1001000":
                leave.setFlag("MO");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("FI");
                        enter.setNote("离岗超时");
                    } else {
                        enter.setFlag("SI");
                    }
//                    enter.setNote("离岗"+m+"分钟");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "1000100":
                leave.setFlag("MO");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() -
                            sdf.parse(leave.getAttendanceTime()).getTime() -
                            sdf.parse(dept.getEndRestTime()).getTime() +
                            sdf.parse(dept.getStartRestTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,下午上班迟到");
                    } else {
                        enter.setNote("下午上班迟到");
                    }
//                    enter.setNote("离岗"+m+"分钟");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "1000010":
                leave.setFlag("MO");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() -
                            sdf.parse(leave.getAttendanceTime()).getTime() -
                            sdf.parse(dept.getEndRestTime()).getTime() +
                            sdf.parse(dept.getStartRestTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,下午上班迟到");
                    } else {
                        enter.setNote("下午上班迟到");
                    }
//                    enter.setNote("离岗"+m+"分钟");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "1000001":
                leave.setFlag("MO");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() -
                            sdf.parse(leave.getAttendanceTime()).getTime() -
                            sdf.parse(dept.getEndRestTime()).getTime() +
                            sdf.parse(dept.getStartRestTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,下午上班迟到");
                    } else {
                        enter.setNote("下午上班迟到");
                    }
//                    enter.setNote("离岗"+m+"分钟");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0110000":
                leave.setFlag("SO");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("FI");
                        enter.setNote("离岗超时");
                    } else {
                        enter.setFlag("SI");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0101000":
                leave.setFlag("SO");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(dept.getStartRestTime()).getTime() - sdf.parse(enter.getAttendanceTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,提前就餐");
                    } else {
                        enter.setNote("提前就餐");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0100100":
                leave.setFlag("SO");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() -
                            sdf.parse(leave.getAttendanceTime()).getTime() -
                            sdf.parse(dept.getEndRestTime()).getTime() +
                            sdf.parse(dept.getStartRestTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,提前就餐,下午上班迟到");
                    } else {
                        enter.setNote("提前就餐,下午上班迟到");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0100010":
                leave.setFlag("SO");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(dept.getEndWorkTime()).getTime() -
                            sdf.parse(leave.getAttendanceTime()).getTime() -
                            sdf.parse(dept.getEndRestTime()).getTime() +
                            sdf.parse(dept.getStartRestTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,提前就餐,下午上班迟到");
                    } else {
                        enter.setNote("提前就餐,下午上班迟到");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0100001":
                leave.setFlag("SO");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    enter.setFlag("FI");
                    int m = (int) Math.ceil((sdf.parse(dept.getEndWorkTime()).getTime() -
                            sdf.parse(leave.getAttendanceTime()).getTime() -
                            sdf.parse(dept.getEndRestTime()).getTime() +
                            sdf.parse(dept.getStartRestTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setNote("离岗超时,提前就餐,下午上班迟到");
                    } else {
                        enter.setNote("提前就餐,下午上班迟到");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0011000":
                leave.setFlag("SO");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("SI");//正常进入
                enter.setNote("下午正常上班");
                break;
            case "0010100":
                leave.setFlag("SO");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("FI");//正常进入
                enter.setNote("下午上班迟到");
                break;
            case "0010010":
                leave.setFlag("SO");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("FI");//正常进入
                enter.setNote("下午旷工");
                break;
            case "0010001":
                leave.setFlag("SO");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("FI");//正常进入
                enter.setNote("下午旷工");
                break;
            case "0001100":
                leave.setFlag("SO");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enter.getAttendanceTime()).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("FI");
                        enter.setNote("离岗超时");
                    } else {
                        enter.setFlag("SI");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0001010":
                leave.setFlag("FO");
                leave.setNote("早退");
                enter.setFlag("SI");
                break;
            case "0001001":
                leave.setFlag("FO");
                leave.setNote("早退");
                enter.setFlag("SI");
                break;
            case "0000110":
                leave.setFlag("SO");
                leave.setNote("正常下班");
                enter.setFlag("MI");
                enter.setNote("下班后进入");
                break;
            case "0000101":
                leave.setFlag("SO");
                leave.setNote("正常下班");
                enter.setFlag("MI");
                enter.setNote("下班后进入");
                break;
            case "0000011":
                leave.setFlag("SO");
                leave.setNote("正常下班");
                enter.setFlag("MI");
                enter.setNote("下班后进入");
                break;
        }
    }
}

class OneEnterOneLeave {
    public AttendanceRecord enter;
    public AttendanceRecord leave;
    public String type;

    public OneEnterOneLeave(AttendanceRecord enter, AttendanceRecord leave) {
        this.enter = enter;
        this.leave = leave;
    }

    public boolean isComplete() {
        if (enter != null && leave != null) {
            return true;
        } else {
            return false;
        }
    }
}

class OneLeaveOneEnter {
    public AttendanceRecord enter;
    public AttendanceRecord leave;
    public String type;

    public OneLeaveOneEnter(AttendanceRecord leave, AttendanceRecord enter) {
        this.enter = enter;
        this.leave = leave;
    }

    public boolean isComplete() {
        if (enter != null && leave != null) {
            return true;
        } else {
            return false;
        }
    }
}