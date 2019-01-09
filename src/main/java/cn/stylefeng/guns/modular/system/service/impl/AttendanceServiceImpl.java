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
import java.util.*;

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

    @Autowired
    private IAttendanceService attendanceRecordService;

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
    public void handleSomeOneAttendRecord(LinkedList<AttendanceRecord> adrs) {
        Dept dept = deptService.getDeptByUserId(adrs.get(0).getUserId());

        List<OneLeaveOneEnter> oeols = new ArrayList<OneLeaveOneEnter>();
        String startWorkId = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        List<AttendanceRecord> collect = new ArrayList<AttendanceRecord>();
        AttendanceRecord tmp = null;
//        int i;
        //找到第一条进入记录 作为上班考勤记录
        while(adrs.size()>0) {
            tmp = adrs.remove();
            collect.add(tmp);

            if (tmp.getAction().equals("1")) {
                startWorkId = tmp.getId();
                String attendTime = tmp.getAttendanceTime().substring(11);
                if (attendTime.compareTo(dept.getStartWorkTime()) <= 0) {
                    tmp.setFlag("SI");
                    tmp.setNote("正常上班");
                } else {
                    tmp.setFlag("FI");
                    try {
                        int m = (int) Math.ceil((sdf.parse(attendTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                        tmp.setNote("上班迟到" + m + "分钟");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            } else {
                tmp.setFlag("MO");
                tmp.setNote("上班前的外出");
            }
        }

        if (adrs.isEmpty()) {
            //仅有一条上班考勤记录
            if(tmp != null){
                if(tmp.getFlag().equals("FI")) {
                    //上班异常则追加备注
                    tmp.setNote(tmp.getNote()+",缺少下班考勤记录");
                } else {
                    //上班正常则替换备注
                    tmp.setFlag("FI");
                    tmp.setNote("缺少下班考勤记录");
                }
            }
        } else if (adrs.size() == 1) {
            //上班后还有一次考勤记录
//            AttendanceRecord ad = adrs.get(adrs.size() - 1);
            tmp = adrs.remove();
            collect.add(tmp);
            if (tmp.getAction().equals("2")) {
                //上班后仅有一次离开 算下班
                if (tmp.getAttendanceTime().substring(11).compareTo(dept.getStartOverTime()) > 0) {
                    //加班
                    tmp.setFlag("FO");
                    try {
                        int m = (int) Math.ceil((sdf.parse(tmp.getAttendanceTime().substring(11)).getTime() - sdf.parse(dept.getStartOverTime()).getTime()) / (1000 * 60));
                        tmp.setNote("加班" + m + "分钟");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (tmp.getAttendanceTime().substring(11).compareTo(dept.getEndWorkTime()) >= 0) {
                    //正常下班
                    tmp.setFlag("SO");
                    tmp.setNote("正常下班");
                } else {
                    //早退
                    tmp.setFlag("FO");
                    try {
                        int m = (int) Math.ceil((sdf.parse(dept.getEndWorkTime()).getTime() - sdf.parse(tmp.getAttendanceTime().substring(11)).getTime()) / (1000 * 60));
                        tmp.setNote("下班早退" + m + "分钟");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //上班有又有一次进入考勤
                tmp.setFlag("FI");
                tmp.setNote("缺少下班考勤记录");
            }
        } else {
            //队列还剩至少2条记录
            //分析上班进入后的数据
            //将剩余打卡记录分成N对 1对是一出一进
            AttendanceRecord firstRecard = null;
            AttendanceRecord secondRecard = null;
            while (adrs.size() > 0) {
                if (adrs.size() > 1) {
                    //还剩2条以上数据
                    firstRecard = adrs.remove();
                    collect.add(firstRecard);

                    secondRecard = adrs.element();

                    String firstAction = firstRecard.getAction();
                    String secondAction = secondRecard.getAction();

                    if (secondAction.equals(firstAction)) {
                        if (firstAction.equals("1")) {
                            //进入进入
                            oeols.add(new OneLeaveOneEnter(null, firstRecard));
                            oeols.add(new OneLeaveOneEnter(null, secondRecard));
                            collect.add(adrs.remove());
                            if (startWorkId == null) {
                                startWorkId = firstRecard.getId();
                            }
                        } else {
                            //离开离开
                            oeols.add(new OneLeaveOneEnter(firstRecard, null));
                        }
                    } else {
                        if (firstAction.equals("1")) {
                            //进入离开
                            oeols.add(new OneLeaveOneEnter(null, firstRecard));
                            if (startWorkId == null) {
                                startWorkId = firstRecard.getId();
                            }
                        } else {
                            //离开进入
                            oeols.add(new OneLeaveOneEnter(firstRecard, secondRecard));
                            collect.add(adrs.remove());
                            if (startWorkId == null) {
                                startWorkId = secondRecard.getId();
                            }
                        }
                    }
                } else {
                    //队列剩余1条记录
                    tmp = adrs.remove();
                    collect.add(tmp);

                    if(tmp.getAction().equals("1")) {
                        //最后剩一条进入
                        tmp.setFlag("FI");
                        tmp.setNote("缺少下班考勤记录");
                    } else {
                        //最后剩一条离开 算下班
                        String a = dept.getStartOverTime();
                        int c = tmp.getAttendanceTime().compareTo(a);
                        if (tmp.getAttendanceTime().substring(11).compareTo(dept.getStartOverTime()) > 0) {
                            //加班
                            tmp.setFlag("FO");
                            try {
                                int m = (int) Math.ceil((sdf.parse(tmp.getAttendanceTime().substring(11)).getTime() - sdf.parse(dept.getStartOverTime()).getTime()) / (1000 * 60));
                                tmp.setNote("加班" + m + "分钟");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (tmp.getAttendanceTime().substring(11).compareTo(dept.getEndWorkTime()) >= 0) {
                            //正常下班
                            tmp.setFlag("SO");
                            tmp.setNote("正常下班");
                        } else {
                            //早退
                            tmp.setFlag("FO");
                            try {
                                int m = (int) Math.ceil((sdf.parse(dept.getEndWorkTime()).getTime() - sdf.parse(tmp.getAttendanceTime().substring(11)).getTime()) / (1000 * 60));
                                tmp.setNote("下班早退" + m + "分钟");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        for (OneLeaveOneEnter o : oeols) {
            if (o.isComplete()) {
                markEnterAndLeave(o, dept, startWorkId);
            }
        }
        int a= 0;
        for(AttendanceRecord b : collect) {
            attendanceRecordService.updateById(b);
        }
    }

    @Override
    public List<Map<String, Object>> statisticsOneDayAttendRecords(String date) {
        return this.baseMapper.statisticsOneDayAttendRecords(date);
    }

    private void markEnterAndLeave(OneLeaveOneEnter o, Dept dept, String startWorkId) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String[] times = {"00:00:00", dept.getStartWorkTime(), dept.getStartRestTime(),
                dept.getEndRestTime(), dept.getEndWorkTime(), dept.getStartOverTime(), "24:00:00"};

        AttendanceRecord enter = o.enter;
        AttendanceRecord leave = o.leave;

        String enterTime = enter.getAttendanceTime().substring(11);
        String leaveTime = leave.getAttendanceTime().substring(11);

        StringBuffer type = new StringBuffer();

        for (int i = 0; i != times.length - 1; i++) {
            type.append("0");
//            int count = 0;
            if (enterTime.compareTo(times[i]) > 0 && enterTime.compareTo(times[i + 1]) <= 0) {
                type.append("1");
//                count += 1;
            }
            if (leaveTime.compareTo(times[i]) > 0 && leaveTime.compareTo(times[i + 1]) <= 0) {
                type.append("1");
//                count += 1;
            }


        }
        switch (type.toString().substring(1)) {

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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() -
                            sdf.parse(leaveTime).getTime() -
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() -
                            sdf.parse(leaveTime).getTime() -
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() -
                            sdf.parse(leaveTime).getTime() -
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
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
                    int m = (int) Math.ceil((sdf.parse(dept.getStartRestTime()).getTime() - sdf.parse(enterTime).getTime()) / (1000 * 60));
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() -
                            sdf.parse(leaveTime).getTime() -
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
                            sdf.parse(leaveTime).getTime() -
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
                            sdf.parse(leaveTime).getTime() -
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
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(leaveTime).getTime()) / (1000 * 60));
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