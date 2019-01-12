package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.AttendanceDetail;
import cn.stylefeng.guns.modular.system.model.AttendanceRecord;
import cn.stylefeng.guns.modular.system.dao.AttendanceRecordMapper;
import cn.stylefeng.guns.modular.system.model.Dept;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private IMonthAttendanceService monthAttendanceService;

    @Autowired
    private IAttendanceDetailService attendanceDetailService;

    @Override
    public List<AttendanceRecord> getOneDayAttendRecords(String date) {

        return this.baseMapper.getOneDayAttendRecords(date);
    }

    @Override
    public void handleSomeOneAttendRecord(LinkedList<AttendanceRecord> adrs, LocalDate date) {
        Dept dept = deptService.getDeptByUserId(adrs.get(0).getUserId());

        List<OneLeaveOneEnter> oeols = new ArrayList<OneLeaveOneEnter>();
        String startWorkId = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        int week = date.getDayOfWeek().getValue();

        List<AttendanceRecord> collect = new ArrayList<AttendanceRecord>();
        AttendanceRecord tmp = null;

        if (dept.getWorkDay().indexOf(week + "") == -1) {
            //非工作日
            collect = new ArrayList<AttendanceRecord>(adrs);
            AttendanceRecord onWork = null;
            AttendanceRecord offWork = null;
            String enterTime = null;
            String leaveTime = null;
            //查找第一次进入记录
            for (int m = 0; m!=collect.size();m++) {
                if (collect.get(m).getAction().equals("1")) {
                    onWork = collect.get(m);
                    enterTime =onWork.getAttendanceTime().substring(11);
                    break;
                }
            }
            //查找第一次离开时间
            for (int n = collect.size()-1; n>=0;n--) {
                if (collect.get(n).getAction().equals("0")) {
                    offWork = collect.get(n);
                    leaveTime =offWork.getAttendanceTime().substring(11);
                    break;
                }
            }

            if (onWork!=null && offWork!=null) {
                if (leaveTime.compareTo(enterTime) >= 0) {
                    //离开时间大于进入时间 正常加班
                    onWork.setFlag("1");
                    onWork.setNote("休息有进出记录,上班时间:" + enterTime);
                    offWork.setFlag("1");
                    offWork.setNote("下班时间:" + leaveTime );
                } else {
                    onWork.setFlag("1");
                    onWork.setNote("休息有进出记录,上班时间:" + enterTime);
                    offWork.setFlag("1");
                    offWork.setNote("缺少下班记录" );
                }
            } else if(onWork!=null && offWork==null) {
                onWork.setFlag("1");
                onWork.setNote("休息有进出记录,上班时间:" + enterTime+",缺少下班记录");
            } else if (onWork==null && offWork!=null) {
                offWork.setFlag("1");
                offWork.setNote("休息有进出记录,缺少上班记录,下班时间:" + leaveTime );
            }
        } else {
            //工作日
            //找到第一条进入记录 作为上班考勤记录
            while(adrs.size()>0) {
                tmp = adrs.remove();
                collect.add(tmp);

                if (tmp.getAction().equals("1")) {
                    startWorkId = tmp.getId();
                    String attendTime = tmp.getAttendanceTime().substring(11);
                    //找到上班时间 判断是否迟到
                    if (attendTime.compareTo(dept.getStartWorkTime()) <= 0) {
                        tmp.setFlag("1");
                        tmp.setNote("上班时间:"+attendTime);
                    } else {
                        tmp.setFlag("1");
                        try {
                            int m = (int) Math.ceil((sdf.parse(attendTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                            tmp.setNote("上班迟到:"+attendTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                } else {
                    tmp.setFlag("0");
                    tmp.setNote("上班前的外出");
                }
            }

            if (adrs.isEmpty()) {
                //仅有一条上班考勤记录
                if(tmp != null){
                    //上班异常则追加备注
                    tmp.setNote(tmp.getNote()+",缺少下班记录");
                }
            } else if (adrs.size() == 1) {
                //上班后还有一次考勤记录
                tmp = adrs.remove();
                collect.add(tmp);
                if (tmp.getAction().equals("2")) {
                    //上班后仅有一次离开 算下班
                    String leaveTime =tmp.getAttendanceTime().substring(11);
                    //判断是否加班
                    if (leaveTime.compareTo(dept.getStartOverTime()) > 0) {
                        //加班
                        tmp.setFlag("1");
                        try {
                            int m = (int) Math.ceil((sdf.parse(leaveTime).getTime() - sdf.parse(dept.getStartOverTime()).getTime()) / (1000 * 60));
                            tmp.setNote("下班时间:"+leaveTime+",加班:" + m + "分钟");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (leaveTime.compareTo(dept.getEndWorkTime()) >= 0) {
                        //正常下班
                        tmp.setFlag("1");
                        tmp.setNote("下班时间:"+leaveTime);
                    } else {
                        //早退
                        tmp.setFlag("1");
                        tmp.setNote("下班早退:"+leaveTime);
                    }
                } else {
                    //上班有又有一次进入考勤
                    tmp.setFlag("1");
                    tmp.setNote("缺少下班记录");
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
                            tmp.setFlag("1");
                            tmp.setNote("缺少下班记录");
                        } else {
                            //最后剩一条离开 算下班
                            String leaveTime =tmp.getAttendanceTime().substring(11);
                            if (leaveTime.compareTo(dept.getStartOverTime()) > 0) {
                                //加班
                                tmp.setFlag("1");
                                try {
                                    int m = (int) Math.ceil((sdf.parse(leaveTime).getTime() - sdf.parse(dept.getStartOverTime()).getTime()) / (1000 * 60));
                                    tmp.setNote("下班时间:"+leaveTime+",加班:" + m + "分钟");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (leaveTime.compareTo(dept.getEndWorkTime()) >= 0) {
                                //正常下班
                                tmp.setFlag("1");
                                tmp.setNote("下班时间:"+leaveTime);
                            } else {
                                //早退
                                tmp.setFlag("1");
                                tmp.setNote("下班早退:"+leaveTime);
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
        }

        for(AttendanceRecord b : collect) {
            attendanceRecordService.updateById(b);
            String note = b.getNote();
            if(note != null && !note.equals("")) {
                String text = note.split(":")[0];
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                if(text.indexOf("时间") == -1 && b.getFlag().equals("1")) {
                    attendanceDetailService.insert(
                            new AttendanceDetail(uuid,
                                    b.getAttendanceTime().substring(0,10),
                                    b.getUserId(),
                                    text));
                }
            }
        }
    }

    @Override
    public boolean statisticsOneDayAttendRecords(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();

        List<Map<String, Object>> statisticsRlt = this.baseMapper.statisticsOneDayAttendRecords(date.toString());

        for (Map<String, Object> tmp : statisticsRlt) {
            String userId = (String) tmp.get("user_id");
            String note = ((String) tmp.get("result")).replace(",", "</br>");

            int day = date.getDayOfMonth();

            MonthAttendance m = monthAttendanceService.getMonthAttendanceByYearMonthUserId(year, month, userId);
            if (m == null) {
                m = new MonthAttendance(UUID.randomUUID().toString().replaceAll("-", ""), year, month, userId);
            }

            Class reflect = m.getClass();
            try {
                Method setDayN = reflect.getMethod("setDay" + day, String.class);
                setDayN.invoke(m, note);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

                boolean a = monthAttendanceService.insertOrUpdate(m);
        }
        return true;
    }

    @Override
    public boolean markAttendanceRecord(LocalDate date) {

        List<AttendanceRecord> ads = attendanceRecordService.getOneDayAttendRecords(date.toString());
        if (ads.size() ==0) {
            return true;
        }
        int begin = 0;
        for (int i = 1; i != ads.size(); i++) {
            if (!ads.get(i).getUserId().equals(ads.get(i - 1).getUserId())) {
                attendanceRecordService.handleSomeOneAttendRecord(new LinkedList(ads.subList(begin, i)), date);
                begin = i;
            }
        }
        if (begin==0) {
            attendanceRecordService.handleSomeOneAttendRecord(new LinkedList(ads), date);
        }

        return true;
    }

    @Override
    public List<AttendanceRecord> getLackDeptInfoAttendanceRecord() {
        return this.baseMapper.getLackDeptInfoAttendanceRecord();
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
            if (enterTime.compareTo(times[i]) > 0 && enterTime.compareTo(times[i + 1]) <= 0) {
                type.append("1");
            }
            if (leaveTime.compareTo(times[i]) > 0 && leaveTime.compareTo(times[i + 1]) <= 0) {
                type.append("1");
            }

        }
        switch (type.toString().substring(1)) {

            case "1100000":
                //上班前一次出入
                leave.setFlag("0");
                leave.setNote("未到上班时间离开");
                enter.setFlag("0");
                enter.setNote("未到上班时间进入");
                break;
            case "1010000":
                leave.setFlag("0");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(dept.getStartWorkTime()).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("1");
                        enter.setNote("离岗超时:"+dept.getStartWorkTime()+"-"+enterTime);
                    } else {
                        enter.setFlag("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "1001000":
                leave.setFlag("0");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                enter.setFlag("1");
                enter.setNote("上午旷工:"+leaveTime+"-"+enterTime+",午休迟到:"+enterTime);
                break;
            case "1000100":
                leave.setFlag("0");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                enter.setFlag("1");
                enter.setNote("上午旷工:"+leaveTime+"-"+enterTime+",午休迟到:"+enterTime);
                break;
            case "1000010":
                leave.setFlag("0");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                enter.setFlag("1");
                enter.setNote("全天旷工:"+leaveTime+"-"+enterTime);
                break;
            case "1000001":
                leave.setFlag("0");//正常离开
                leave.setNote("未到上班时间离开");//正常离开
                enter.setFlag("1");
                enter.setNote("全天旷工:"+leaveTime+"-"+enterTime);
                break;
            //=============================================================================
            case "0110000":
                leave.setFlag("0");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(leaveTime).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("1");
                        enter.setNote("离岗超时:"+leaveTime+"-"+enterTime);
                    } else {
                        enter.setFlag("0");
                        enter.setNote("离岗返回");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0101000":
                leave.setFlag("1");//正常离开
                leave.setNote("提前就餐:"+leaveTime);//正常离开
                enter.setFlag("1");
                enter.setNote("午休上班时间:"+enterTime);
                break;
            case "0100100":
                leave.setFlag("1");//正常离开
                leave.setNote("提前就餐:"+leaveTime);//正常离开
                enter.setFlag("1");
                enter.setNote("午休迟到:"+enterTime);
                break;
            case "0100010":
                leave.setFlag("1");//正常离开
                leave.setNote("提前就餐:"+leaveTime);//正常离开
                enter.setFlag("1");
                enter.setNote("下午旷工:"+leaveTime+"-"+enterTime);
                break;
            case "0100001":
                leave.setFlag("1");//正常离开
                leave.setNote("提前就餐:"+leaveTime);//正常离开
                enter.setFlag("1");
                enter.setNote("下午旷工:"+leaveTime+"-"+enterTime);
                break;
            //=============================================================================
            case "0011000":
                leave.setFlag("0");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("1");//正常进入
                enter.setNote("午休上班时间:"+enterTime);
                break;
            case "0010100":
                leave.setFlag("0");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("1");//正常进入
                enter.setNote("午休迟到:"+enterTime);
                break;
            case "0010010":
                leave.setFlag("0");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("1");//正常进入
                enter.setNote("下午旷工:"+leaveTime+"-"+enterTime);
                break;
            case "0010001":
                leave.setFlag("0");//正常离开
                leave.setNote("正常就餐");//正常离开
                enter.setFlag("1");//正常进入
                enter.setNote("下午旷工:"+leaveTime+"-"+enterTime);
                break;
           //=============================================================================
            case "0001100":
                leave.setFlag("0");//正常离开
                leave.setNote("离岗");//正常离开
                try {
                    int m = (int) Math.ceil((sdf.parse(enterTime).getTime() - sdf.parse(leaveTime).getTime()) / (1000 * 60));
                    if (m > dept.getLeaveTime()) {
                        enter.setFlag("1");
                        enter.setNote("离岗超时:"+leaveTime+"-"+enterTime);
                    } else {
                        enter.setFlag("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "0001010":
                leave.setFlag("1");
                leave.setNote("下班早退:"+leaveTime);
                enter.setFlag("0");
                break;
            case "0001001":
                leave.setFlag("1");
                leave.setNote("下班早退:"+leaveTime);
                enter.setFlag("0");
                break;
            //=============================================================================
            case "0000110":
                leave.setFlag("0");
                leave.setNote("下班时间:"+leaveTime);
                enter.setFlag("0");
                enter.setNote("下班后进入");
                break;
            case "0000101":
                leave.setFlag("1");
                leave.setNote("下班时间:"+leaveTime);
                enter.setFlag("0");
                enter.setNote("下班后进入");
                break;
            //=============================================================================
            case "0000011":
                leave.setFlag("1");
                leave.setNote("下班时间:"+leaveTime);
                enter.setFlag("0");
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