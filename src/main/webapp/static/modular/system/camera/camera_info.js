/**
 * 初始化设备监控详情对话框
 */
var CameraInfoDlg = {
    cameraInfoData : {}
};

/**
 * 清除数据
 */
CameraInfoDlg.clearData = function() {
    this.cameraInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CameraInfoDlg.set = function(key, val) {
    this.cameraInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CameraInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CameraInfoDlg.close = function() {
    parent.layer.close(window.parent.Camera.layerIndex);
}

/**
 * 收集数据
 */
CameraInfoDlg.collectData = function() {
    this
    .set('uuid')
    .set('cameraId')
    .set('address')
    .set('status');
}

/**
 * 提交添加
 */
CameraInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/camera/add", function(data){
        Feng.success("添加成功!");
        window.parent.Camera.table.refresh();
        CameraInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cameraInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CameraInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/camera/update", function(data){
        Feng.success("修改成功!");
        window.parent.Camera.table.refresh();
        CameraInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cameraInfoData);
    ajax.start();
}

$(function() {

});
