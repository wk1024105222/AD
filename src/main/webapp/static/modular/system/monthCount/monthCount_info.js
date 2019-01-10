/**
 * 初始化月度统计表详情对话框
 */
var MonthCountInfoDlg = {
    monthCountInfoData : {}
};

/**
 * 清除数据
 */
MonthCountInfoDlg.clearData = function() {
    this.monthCountInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonthCountInfoDlg.set = function(key, val) {
    this.monthCountInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonthCountInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MonthCountInfoDlg.close = function() {
    parent.layer.close(window.parent.MonthCount.layerIndex);
}

/**
 * 收集数据
 */
MonthCountInfoDlg.collectData = function() {
    this
    .set('id')
    .set('month')
    .set('userId')
    .set('userName')
    .set('company')
    .set('department')
    .set('team')
    .set('times')
    .set('type');
}

/**
 * 提交添加
 */
MonthCountInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monthCount/add", function(data){
        Feng.success("添加成功!");
        window.parent.MonthCount.table.refresh();
        MonthCountInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monthCountInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MonthCountInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monthCount/update", function(data){
        Feng.success("修改成功!");
        window.parent.MonthCount.table.refresh();
        MonthCountInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monthCountInfoData);
    ajax.start();
}

$(function() {

});
