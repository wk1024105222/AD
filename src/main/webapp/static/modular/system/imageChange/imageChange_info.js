/**
 * 初始化影像管理详情对话框
 */
var ImageChangeInfoDlg = {
    imageChangeInfoData : {}
};

/**
 * 清除数据
 */
ImageChangeInfoDlg.clearData = function() {
    this.imageChangeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ImageChangeInfoDlg.set = function(key, val) {
    this.imageChangeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ImageChangeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ImageChangeInfoDlg.close = function() {
    parent.layer.close(window.parent.ImageChange.layerIndex);
}

/**
 * 收集数据
 */
ImageChangeInfoDlg.collectData = function() {
    this
    .set('imgName')
    .set('actionType')
    .set('userId')
    .set('createTime')
    .set('sendFlag')
    .set('receiveFlag')
    .set('updateTime')
    .set('imageId');
}

/**
 * 提交添加
 */
ImageChangeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/imageChange/add", function(data){
        Feng.success("添加成功!");
        window.parent.ImageChange.table.refresh();
        ImageChangeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.imageChangeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ImageChangeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/imageChange/update", function(data){
        Feng.success("修改成功!");
        window.parent.ImageChange.table.refresh();
        ImageChangeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.imageChangeInfoData);
    ajax.start();
}

$(function() {

});
