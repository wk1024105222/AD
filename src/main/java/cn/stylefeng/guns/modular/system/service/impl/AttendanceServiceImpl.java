package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.dao.AttendanceRecordMapper;
import cn.stylefeng.guns.modular.system.model.*;
import cn.stylefeng.guns.modular.system.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private IDeptService deptService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IAttendanceService attendanceRecordService;

    @Autowired
    private IMonthAttendanceService monthAttendanceService;

    @Autowired
    private IAttendanceDetailService attendanceDetailService;


    /**
     * 获取指定日期的所有考勤记录
     * @param date
     * @return
     */
    @Override
    public List<AttendanceRecord> getOneDayAttendRecords(String date) {

        return this.baseMapper.getOneDayAttendRecords(date);
    }

    /**
     * 更新指定日期某个人的所有考勤记录备注
     * @param adrs
     * @param date
     */
    @Override
    public void handleSomeOneAttendRecord(LinkedList<AttendanceRecord> adrs, LocalDate date) {
        Dept dept = deptService.getDeptByUserId(adrs.get(0).getUserId());

        List<OneLeaveOneEnter> oeols = new ArrayList<OneLeaveOneEnter>();
        String startWorkId = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        int week = date.getDayOfWeek().getValue();

        List<AttendanceRecord> collect = new ArrayList<AttendanceRecord>();
        AttendanceRecord tmp = null;

        if (dept.getWorkDay().indexOf(String.valueOf(week)) == -1) {
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
                if (collect.get(n).getAction().equals("2")) {
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
                    offWork.setNote("下班时间:" + leaveTime +",加班:"+enterTime+"-"+leaveTime);
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
                String[] arr =note.split(",");
                for (String a : arr) {
                    String text = a.split(":")[0];
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
    }

    /**
     * 统计指定日期的月度考勤表
     * @param date
     * @return
     */
    @Override
    public int statisticsOneDayAttendRecords(LocalDate date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int dayOfWeek = date.getDayOfWeek().getValue();

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

        //group by 聚合数据 已员工为主体 左连接 聚合结果
        List<Map<String, Object>> statisticsRlt = this.baseMapper.statisticsOneDayAttendRecords(date.toString());

//        Map<String,String> userIdToAttendTotal = new HashMap<String,String>();
////        for (Map<String, Object> tmp : statisticsRlt) {
////            Object o = tmp.get("result");
////            if(o != null && !((String)o).equals("")) {
////                userIdToAttendTotal.put((String) tmp.get("user_id"),((String)o).replace(",", "</br>"));
////            } else {
////                userIdToAttendTotal.put((String) tmp.get("user_id"),null);
////            }
////        }
        //查出指定月份所有人的月度考勤记录
        List<MonthAttendance> mas = monthAttendanceService.getMonthAttendanceByYearMonthUserId(year, month, null);
        Map<String,MonthAttendance> uesrIdToMonthAttend = new HashMap<String,MonthAttendance>();
        //转成Map<userid,MonthAttendance> 方便后面获取
        for (MonthAttendance ma : mas) {
            uesrIdToMonthAttend.put(ma.getUserId(),ma);
        }

        User user = null;
        Dept dept = null;
        String workDays = null;
        MonthAttendance ma = null;
        int rank = 0;
        String pids = null;

        for (Map<String, Object> tmp : statisticsRlt) {
            String userId = (String) tmp.get("user_id");
            String note = "";
            user = idToUser.get(userId);
            if(user != null) {
                dept = idToDept.get(idToUser.get(userId).getDeptid().toString());
            }

            if (tmp.get("result") != null && !((String) tmp.get("result")).equals("")) {
                note = ((String) tmp.get("result")).replace(",", "</br>");
            } else {
                //无考勤记录需判断是否为休息日
                workDays = dept.getWorkDay();
                if (dept != null) {
                    if (workDays.indexOf(String.valueOf(dayOfWeek) ) == -1) {
                        //休息日
                        note = "休息";
                    } else {
                        note = "全天旷工";
                        attendanceDetailService.insert(
                                new AttendanceDetail(UUID.randomUUID().toString().replaceAll("-", ""),
                                        date.format(dtf),
                                        userId,
                                        note));
                    }
                }
            }

            ma = uesrIdToMonthAttend.get(userId);
            if (ma == null) {
                ma = new MonthAttendance();

                ma.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                ma.setYear(year);
                ma.setMonth(month);
                ma.setUserId(userId);
                ma.setUserName(user.getName());

                pids = dept.getPids();
                rank = pids.split(",").length;
                if(rank == 3) {
                    int index1=pids.indexOf(',');
                    int index2=pids.indexOf(',',index1+1);
                    ma.setCompany(idToDept.get(pids.substring(index1+2,index2-1)).getSimplename());
                    ma.setDepartment(idToDept.get(pids.substring(index2+2,pids.length()-2)).getSimplename());
                    ma.setTeam(idToDept.get(user.getDeptid().toString()).getSimplename());

                } else if(rank == 2) {
                    int index1=pids.indexOf(',');
                    ma.setCompany(idToDept.get(pids.substring(index1+2,pids.length()-2)).getSimplename());
                    ma.setDepartment(idToDept.get(user.getDeptid().toString()).getSimplename());
                } else {
                    ma.setCompany(idToDept.get(user.getDeptid().toString()).getSimplename());
                }
            }

            Class reflect = ma.getClass();
            try {
                Method setDayN = reflect.getMethod("setDay" + day, String.class);
                setDayN.invoke(ma, note);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

                boolean a = monthAttendanceService.insertOrUpdate(ma);
        }
        return statisticsRlt.size();
    }

    /**
     * 更新指定日期的考勤记录备注
     * @param date
     * @return 处理数量
     */
    @Override
    public int markAttendanceRecord(LocalDate date) {
        //获取指定日期的所有考勤记录
        List<AttendanceRecord> ads = attendanceRecordService.getOneDayAttendRecords(date.toString());
        if (ads.size() ==0) {
            return 0;
        }
        int begin = 0;
        for (int i = 1; i != ads.size(); i++) {
            if (!ads.get(i).getUserId().equals(ads.get(i - 1).getUserId())) {
                //更新指定日期某个人的所有考勤记录备注
                attendanceRecordService.handleSomeOneAttendRecord(new LinkedList(ads.subList(begin, i)), date);
                begin = i;
            }
        }
        if (begin==0) {
            attendanceRecordService.handleSomeOneAttendRecord(new LinkedList(ads), date);
        } else {
            attendanceRecordService.handleSomeOneAttendRecord(new LinkedList(ads.subList(begin,ads.size())), date);
        }

        return ads.size();
    }

    @Override
    public List<AttendanceRecord> getLackDeptInfoAttendanceRecord() {
        return this.baseMapper.getLackDeptInfoAttendanceRecord();
    }

    /**
     * 补充考勤记录的个人信息 姓名 公司 部门 组
     * @return
     */
    @Override
    public int fillAttendRecordDeptInfo() {
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

        //获取确实姓名的考勤数据
        List<AttendanceRecord> adrs = attendanceRecordService.getLackDeptInfoAttendanceRecord();
        User user = null;
        Dept dept = null;
        for (AttendanceRecord adr:adrs) {
            if(adr.getUserId() != null) {
                user = idToUser.get(adr.getUserId());
                adr.setUserName(user.getName());
                dept = idToDept.get(user.getDeptid().toString());
                String pids = dept.getPids();
                int rank = pids.split(",").length;
                if(rank == 3) {
                    int index1=pids.indexOf(',');
                    int index2=pids.indexOf(',',index1+1);
                    adr.setCompany(idToDept.get(pids.substring(index1+2,index2-1)).getSimplename());
                    adr.setDepartment(idToDept.get(pids.substring(index2+2,pids.length()-2)).getSimplename());
                    adr.setTeam(idToDept.get(user.getDeptid().toString()).getSimplename());

                } else if(rank == 2) {
                    int index1=pids.indexOf(',');
                    adr.setCompany(idToDept.get(pids.substring(index1+2,pids.length()-2)).getSimplename());
                    adr.setDepartment(idToDept.get(user.getDeptid().toString()).getSimplename());
                } else {
                    adr.setCompany(idToDept.get(user.getDeptid().toString()).getSimplename());
                }
                attendanceRecordService.updateById(adr);
                logger.info("考勤个人信息补充完毕"+adr.toString());
            } else {
                logger.info("无法根据人脸引擎imgid:"+adr.getImgId()+"找到对应人员");
            }

        }
        return adrs.size();
    }

    @Override
    public List<AttendanceRecord> getList(String user, String date, Integer deptId) {
        return this.baseMapper.getList(user, date, deptId) ;
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