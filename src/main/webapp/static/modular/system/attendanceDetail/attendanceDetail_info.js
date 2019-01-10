/**
 * 初始化考勤明细详情对话框
 */
var AttendanceDetailInfoDlg = {
    attendanceDetailInfoData : {}
};

/**
 * 清除数据
 */
AttendanceDetailInfoDlg.clearData = function() {
    this.attendanceDetailInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AttendanceDetailInfoDlg.set = function(key, val) {
    this.attendanceDetailInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AttendanceDetailInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AttendanceDetailInfoDlg.close = function() {
    parent.layer.close(window.parent.AttendanceDetail.layerIndex);
}

/**
 * 收集数据
 */
AttendanceDetailInfoDlg.collectData = function() {
    this
    .set('id')
    .set('date')
    .set('userId')
    .set('type');
}

/**
 * 提交添加
 */
AttendanceDetailInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/attendanceDetail/add", function(data){
        Feng.success("添加成功!");
        window.parent.AttendanceDetail.table.refresh();
        AttendanceDetailInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.attendanceDetailInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
AttendanceDetailInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/attendanceDetail/update", function(data){
        Feng.success("修改成功!");
        window.parent.AttendanceDetail.table.refresh();
        AttendanceDetailInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.attendanceDetailInfoData);
    ajax.start();
}

$(function() {

});
