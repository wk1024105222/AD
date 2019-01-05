/**
 * 初始化月度考勤表详情对话框
 */
var MonthAttendanceInfoDlg = {
    monthAttendanceInfoData : {}
};

/**
 * 清除数据
 */
MonthAttendanceInfoDlg.clearData = function() {
    this.monthAttendanceInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonthAttendanceInfoDlg.set = function(key, val) {
    this.monthAttendanceInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonthAttendanceInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MonthAttendanceInfoDlg.close = function() {
    parent.layer.close(window.parent.MonthAttendance.layerIndex);
}

/**
 * 收集数据
 */
MonthAttendanceInfoDlg.collectData = function() {
    this
    .set('uuid')
    .set('year')
    .set('month')
    .set('days')
    .set('userId')
    .set('userName')
    .set('company')
    .set('department')
    .set('team')
    .set('day1')
    .set('day2')
    .set('day3')
    .set('day4')
    .set('day5')
    .set('day6')
    .set('day7')
    .set('day8')
    .set('day9')
    .set('day10')
    .set('day11')
    .set('day12')
    .set('day13')
    .set('day14')
    .set('day15')
    .set('day16')
    .set('day17')
    .set('day18')
    .set('day19')
    .set('day20')
    .set('day21')
    .set('day22')
    .set('day23')
    .set('day24')
    .set('day25')
    .set('day26')
    .set('day27')
    .set('day28')
    .set('day29')
    .set('day30')
    .set('day31');
}

/**
 * 提交添加
 */
MonthAttendanceInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monthAttendance/add", function(data){
        Feng.success("添加成功!");
        window.parent.MonthAttendance.table.refresh();
        MonthAttendanceInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monthAttendanceInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MonthAttendanceInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monthAttendance/update", function(data){
        Feng.success("修改成功!");
        window.parent.MonthAttendance.table.refresh();
        MonthAttendanceInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monthAttendanceInfoData);
    ajax.start();
}

$(function() {

});
