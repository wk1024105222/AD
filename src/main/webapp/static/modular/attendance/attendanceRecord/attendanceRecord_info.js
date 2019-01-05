/**
 * 初始化出入记录详情对话框
 */
var AttendanceRecordInfoDlg = {
    attendanceRecordInfoData : {}
};

/**
 * 清除数据
 */
AttendanceRecordInfoDlg.clearData = function() {
    this.attendanceRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AttendanceRecordInfoDlg.set = function(key, val) {
    this.attendanceRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AttendanceRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AttendanceRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.AttendanceRecord.layerIndex);
}

/**
 * 收集数据
 */
AttendanceRecordInfoDlg.collectData = function() {
    this
    .set('uuid')
    .set('attendanceTime')
    .set('userId')
    .set('type')
    .set('imgPath')
    .set('company')
    .set('department')
    .set('team')
    .set('note');
}

/**
 * 提交添加
 */
AttendanceRecordInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/attendanceRecord/add", function(data){
        Feng.success("添加成功!");
        window.parent.AttendanceRecord.table.refresh();
        AttendanceRecordInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.attendanceRecordInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
AttendanceRecordInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/attendanceRecord/update", function(data){
        Feng.success("修改成功!");
        window.parent.AttendanceRecord.table.refresh();
        AttendanceRecordInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.attendanceRecordInfoData);
    ajax.start();
}

$(function() {

});
