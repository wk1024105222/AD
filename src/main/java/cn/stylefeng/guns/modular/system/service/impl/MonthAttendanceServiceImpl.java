package cn.stylefeng.guns.modular.system.service.impl;

import cn.stylefeng.guns.modular.system.dao.MonthAttendanceMapper;
import cn.stylefeng.guns.modular.system.model.MonthAttendance;
import cn.stylefeng.guns.modular.system.service.IMonthAttendanceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.CellStyle;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 月度考勤表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-05
 */
@Service
public class MonthAttendanceServiceImpl extends ServiceImpl<MonthAttendanceMapper, MonthAttendance> implements IMonthAttendanceService {

    @Override
    public List<MonthAttendance> getMonthAttendanceByYearMonthUserId(Integer year, Integer month, String userId) {
        return this.baseMapper.getMonthAttendanceByYearMonthUserId(year,month,userId);
    }

    @Override
    public List<MonthAttendance> getMonthAttendanceByYearMonthDeptId(Integer year, Integer month, Integer deptId) {
        return this.baseMapper.getMonthAttendanceByYearMonthDeptId(year,month, deptId);
    }

    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(1,6*256);
        sheet.setColumnWidth(2,6*256);
        sheet.setColumnWidth(3,12*256);
        sheet.setColumnWidth(4,12*256);
        sheet.setColumnWidth(5,24*256);
        sheet.setColumnWidth(6,16*256);
        sheet.setColumnWidth(7,12*256);
        sheet.setColumnWidth(8,22*256);
        sheet.setColumnWidth(9,22*256);
        sheet.setColumnWidth(10,22*256);
        sheet.setColumnWidth(11,22*256);
        sheet.setColumnWidth(12,22*256);
        sheet.setColumnWidth(13,22*256);
        sheet.setColumnWidth(14,22*256);
        sheet.setColumnWidth(15,22*256);
        sheet.setColumnWidth(16,22*256);
        sheet.setColumnWidth(17,22*256);
        sheet.setColumnWidth(18,22*256);
        sheet.setColumnWidth(19,22*256);
        sheet.setColumnWidth(20,22*256);
        sheet.setColumnWidth(21,22*256);
        sheet.setColumnWidth(22,22*256);
        sheet.setColumnWidth(23,22*256);
        sheet.setColumnWidth(24,22*256);
        sheet.setColumnWidth(25,22*256);
        sheet.setColumnWidth(26,22*256);
        sheet.setColumnWidth(27,22*256);
        sheet.setColumnWidth(28,22*256);
        sheet.setColumnWidth(29,22*256);
        sheet.setColumnWidth(30,22*256);
        sheet.setColumnWidth(31,22*256);
        sheet.setColumnWidth(32,22*256);
        sheet.setColumnWidth(33,22*256);
        sheet.setColumnWidth(34,22*256);
        sheet.setColumnWidth(35,22*256);
        sheet.setColumnWidth(36,22*256);
        sheet.setColumnWidth(37,22*256);
        sheet.setColumnWidth(38,22*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
//        font.set
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框


        HSSFCell cell;
        cell=row.createCell(0);cell.setCellValue("序号");cell.setCellStyle(style);
        cell=row.createCell(1);cell.setCellValue("年份");cell.setCellStyle(style);
        cell=row.createCell(2);cell.setCellValue("月份");cell.setCellStyle(style);
        cell=row.createCell(3);cell.setCellValue("工号");cell.setCellStyle(style);
        cell=row.createCell(4);cell.setCellValue("姓名");cell.setCellStyle(style);
        cell=row.createCell(5);cell.setCellValue("公司");cell.setCellStyle(style);
        cell=row.createCell(6);cell.setCellValue("部分");cell.setCellStyle(style);
        cell=row.createCell(7);cell.setCellValue("组");cell.setCellStyle(style);
        cell=row.createCell(8);cell.setCellValue("1");cell.setCellStyle(style);
        cell=row.createCell(9);cell.setCellValue("2");cell.setCellStyle(style);
        cell=row.createCell(10);cell.setCellValue("3");cell.setCellStyle(style);
        cell=row.createCell(11);cell.setCellValue("4");cell.setCellStyle(style);
        cell=row.createCell(12);cell.setCellValue("5");cell.setCellStyle(style);
        cell=row.createCell(13);cell.setCellValue("6");cell.setCellStyle(style);
        cell=row.createCell(14);cell.setCellValue("7");cell.setCellStyle(style);
        cell=row.createCell(15);cell.setCellValue("8");cell.setCellStyle(style);
        cell=row.createCell(16);cell.setCellValue("9");cell.setCellStyle(style);
        cell=row.createCell(17);cell.setCellValue("10");cell.setCellStyle(style);
        cell=row.createCell(18);cell.setCellValue("11");cell.setCellStyle(style);
        cell=row.createCell(19);cell.setCellValue("12");cell.setCellStyle(style);
        cell=row.createCell(20);cell.setCellValue("13");cell.setCellStyle(style);
        cell=row.createCell(21);cell.setCellValue("14");cell.setCellStyle(style);
        cell=row.createCell(22);cell.setCellValue("15");cell.setCellStyle(style);
        cell=row.createCell(23);cell.setCellValue("16");cell.setCellStyle(style);
        cell=row.createCell(24);cell.setCellValue("17");cell.setCellStyle(style);
        cell=row.createCell(25);cell.setCellValue("18");cell.setCellStyle(style);
        cell=row.createCell(26);cell.setCellValue("19");cell.setCellStyle(style);
        cell=row.createCell(27);cell.setCellValue("20");cell.setCellStyle(style);
        cell=row.createCell(28);cell.setCellValue("21");cell.setCellStyle(style);
        cell=row.createCell(29);cell.setCellValue("22");cell.setCellStyle(style);
        cell=row.createCell(30);cell.setCellValue("23");cell.setCellStyle(style);
        cell=row.createCell(31);cell.setCellValue("24");cell.setCellStyle(style);
        cell=row.createCell(32);cell.setCellValue("25");cell.setCellStyle(style);
        cell=row.createCell(33);cell.setCellValue("26");cell.setCellStyle(style);
        cell=row.createCell(34);cell.setCellValue("27");cell.setCellStyle(style);
        cell=row.createCell(35);cell.setCellValue("28");cell.setCellStyle(style);
        cell=row.createCell(36);cell.setCellValue("29");cell.setCellStyle(style);
        cell=row.createCell(37);cell.setCellValue("30");cell.setCellStyle(style);
        cell=row.createCell(38);cell.setCellValue("31");cell.setCellStyle(style);
    }

    @Override
    public boolean exportMonthAttendanceReportXls(HSSFWorkbook workbook, List<MonthAttendance> ads) {
        boolean result = false;
//        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("月度考勤表");
        createTitle(workbook,sheet);

        HSSFCellStyle cellStyle1=workbook.createCellStyle();
        cellStyle1.setWrapText(true);
        cellStyle1.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle1.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle1.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle1.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle cellStyle2=workbook.createCellStyle();
        cellStyle2.setWrapText(true);
        cellStyle2.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle2.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle2.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle2.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        HSSFCellStyle cellStyle= null;

        //新增数据行，并且设置单元格数据
        int rowNum=1;
        for(MonthAttendance mad:ads){
            HSSFRow row = sheet.createRow(rowNum);
            row.setHeight((short) (15.625*17*5));
            HSSFCell cell;
            if (rowNum % 2 == 1) {
                cellStyle = cellStyle1;
            } else {
                cellStyle = cellStyle2;
            }

            cell=row.createCell(0);cell.setCellValue(rowNum);cell.setCellStyle(cellStyle);
            cell=row.createCell(1);if(mad.getYear()!=null){cell.setCellValue(mad.getYear());}cell.setCellStyle(cellStyle);
            cell=row.createCell(2);if(mad.getMonth()!=null){cell.setCellValue(mad.getMonth());}cell.setCellStyle(cellStyle);
            cell=row.createCell(3);if(mad.getUserId()!=null){cell.setCellValue(mad.getUserId());}cell.setCellStyle(cellStyle);
            cell=row.createCell(4);if(mad.getUserName()!=null){cell.setCellValue(mad.getUserName());}cell.setCellStyle(cellStyle);
            cell=row.createCell(5);if(mad.getCompany()!=null){cell.setCellValue(mad.getCompany());}cell.setCellStyle(cellStyle);
            cell=row.createCell(6);if(mad.getDepartment()!=null){cell.setCellValue(mad.getDepartment());}cell.setCellStyle(cellStyle);
            cell=row.createCell(7);if(mad.getTeam()!=null){cell.setCellValue(mad.getTeam());}cell.setCellStyle(cellStyle);
            cell=row.createCell(8);if(mad.getDay1()!=null){cell.setCellValue(mad.getDay1().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(9);if(mad.getDay2()!=null){cell.setCellValue(mad.getDay2().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(10);if(mad.getDay3()!=null){cell.setCellValue(mad.getDay3().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(11);if(mad.getDay4()!=null){cell.setCellValue(mad.getDay4().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(12);if(mad.getDay5()!=null){cell.setCellValue(mad.getDay5().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(13);if(mad.getDay6()!=null){cell.setCellValue(mad.getDay6().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(14);if(mad.getDay7()!=null){cell.setCellValue(mad.getDay7().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(15);if(mad.getDay8()!=null){cell.setCellValue(mad.getDay8().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(16);if(mad.getDay9()!=null){cell.setCellValue(mad.getDay9().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(17);if(mad.getDay10()!=null){cell.setCellValue(mad.getDay10().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(18);if(mad.getDay11()!=null){cell.setCellValue(mad.getDay11().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(19);if(mad.getDay12()!=null){cell.setCellValue(mad.getDay12().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(20);if(mad.getDay13()!=null){cell.setCellValue(mad.getDay13().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(21);if(mad.getDay14()!=null){cell.setCellValue(mad.getDay14().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(22);if(mad.getDay15()!=null){cell.setCellValue(mad.getDay15().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(23);if(mad.getDay16()!=null){cell.setCellValue(mad.getDay16().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(24);if(mad.getDay17()!=null){cell.setCellValue(mad.getDay17().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(25);if(mad.getDay18()!=null){cell.setCellValue(mad.getDay18().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(26);if(mad.getDay19()!=null){cell.setCellValue(mad.getDay19().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(27);if(mad.getDay20()!=null){cell.setCellValue(mad.getDay20().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(28);if(mad.getDay21()!=null){cell.setCellValue(mad.getDay21().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(29);if(mad.getDay22()!=null){cell.setCellValue(mad.getDay22().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(30);if(mad.getDay23()!=null){cell.setCellValue(mad.getDay23().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(31);if(mad.getDay24()!=null){cell.setCellValue(mad.getDay24().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(32);if(mad.getDay25()!=null){cell.setCellValue(mad.getDay25().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(33);if(mad.getDay26()!=null){cell.setCellValue(mad.getDay26().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(34);if(mad.getDay27()!=null){cell.setCellValue(mad.getDay27().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(35);if(mad.getDay28()!=null){cell.setCellValue(mad.getDay28().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(36);if(mad.getDay29()!=null){cell.setCellValue(mad.getDay29().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(37);if(mad.getDay30()!=null){cell.setCellValue(mad.getDay30().replace("</br>","\n"));}cell.setCellStyle(cellStyle);
            cell=row.createCell(38);if(mad.getDay31()!=null){cell.setCellValue(mad.getDay31().replace("</br>","\n"));}cell.setCellStyle(cellStyle);

            rowNum++;
        }

        return result;
    }

    @Override
    public List<MonthAttendance> list(String user, Integer year, Integer month, Integer deptId) {
        return this.baseMapper.list(user,year,month,deptId);
    }
}
