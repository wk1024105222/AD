package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.model.Dept;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.model.MonthCount;
import cn.stylefeng.guns.modular.system.dao.MonthCountMapper;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.service.IDeptService;
import cn.stylefeng.guns.modular.system.service.IMonthCountService;
import cn.stylefeng.guns.modular.system.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

            if (dept != null) {
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
        }

        return maps.size();
    }

    @Override
    public List<MonthCount> list(String user, String month, String type, Integer deptId) {
        return this.baseMapper.list(user,month, type,deptId);
    }

    @Override
    public boolean exportMonthCountReportXls(HSSFWorkbook workbook, List<MonthCount> records) {
        boolean result = false;
//        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("月度统计表");

        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(2,12*256);
        sheet.setColumnWidth(3,24*256);
        sheet.setColumnWidth(4,16*256);
        sheet.setColumnWidth(5,12*256);
        sheet.setColumnWidth(6,6*256);
        sheet.setColumnWidth(7,100*256);


        createTitle(workbook,sheet,0,"出勤月度统计表");

        HSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //新增数据行，并且设置单元格数据
        int rowNum=1;
        String type = "";
        int childRowNum = -1;
        for(MonthCount mc:records){
            if(!mc.getType().equals(type)) {
                childRowNum =1;
                createTitle(workbook,sheet,rowNum++,mc.getType());
                createChildTitle(workbook, sheet,rowNum++);
                type = mc.getType();
            } else {
                HSSFRow row = sheet.createRow(rowNum);
                row.setHeight((short) (15.625*8*5));
                HSSFCell cell;
                cell=row.createCell(0);cell.setCellValue(childRowNum);cell.setCellStyle(cellStyle);
                cell=row.createCell(1);cell.setCellValue(mc.getUserName());cell.setCellStyle(cellStyle);
                cell=row.createCell(2);cell.setCellValue(mc.getUserId());cell.setCellStyle(cellStyle);
                cell=row.createCell(3);cell.setCellValue(mc.getCompany());cell.setCellStyle(cellStyle);
                cell=row.createCell(4);cell.setCellValue(mc.getDepartment());cell.setCellStyle(cellStyle);
                cell=row.createCell(5);cell.setCellValue(mc.getTeam());cell.setCellStyle(cellStyle);
                cell=row.createCell(6);cell.setCellValue(mc.getTimes());cell.setCellStyle(cellStyle);
                cell=row.createCell(7);cell.setCellValue(mc.getDates());cell.setCellStyle(cellStyle);
                childRowNum +=1;
                rowNum++;
            }

        }
        return result;
    }

    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet, int num, String text) {

        HSSFRow row = sheet.createRow(num);
//        row.setHeight((short) (15.625*6*5));

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        CellRangeAddress region1 = new CellRangeAddress(num, num, (short) 0, (short) 7);
        sheet.addMergedRegion(region1);

        HSSFCell cell;
        cell=row.createCell(0);cell.setCellValue(text);cell.setCellStyle(style);

    }

    private void createChildTitle(HSSFWorkbook workbook, HSSFSheet sheet, int num) {
        HSSFRow row = sheet.createRow(num);
//        row.setHeight((short) (15.625*6*5));
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        HSSFCell cell;
        cell=row.createCell(0);cell.setCellValue("序号");cell.setCellStyle(style);
        cell=row.createCell(1);cell.setCellValue("姓名");cell.setCellStyle(style);
        cell=row.createCell(2);cell.setCellValue("工号");cell.setCellStyle(style);
        cell=row.createCell(3);cell.setCellValue("公司");cell.setCellStyle(style);
        cell=row.createCell(4);cell.setCellValue("部门");cell.setCellStyle(style);
        cell=row.createCell(5);cell.setCellValue("组");cell.setCellStyle(style);
        cell=row.createCell(6);cell.setCellValue("次数");cell.setCellStyle(style);
        cell=row.createCell(7);cell.setCellValue("日期");cell.setCellStyle(style);
    }
}
