/**
 * 设备监控管理初始化
 */
var Camera = {
    id: "CameraTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Camera.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'uuid', visible: true, align: 'center', valign: 'middle'},
            {title: '设备编号', field: 'cameraId', visible: true, align: 'center', valign: 'middle'},
            {title: '地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'statusName', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Camera.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Camera.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加设备监控
 */
Camera.openAddCamera = function () {
    var index = layer.open({
        type: 2,
        title: '添加设备监控',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/camera/camera_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看设备监控详情
 */
Camera.openCameraDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '设备监控详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/camera/camera_update/' + Camera.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除设备监控
 */
Camera.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/camera/delete", function (data) {
            Feng.success("删除成功!");
            Camera.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("cameraId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询设备监控列表
 */
Camera.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Camera.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Camera.initColumn();
    var table = new BSTable(Camera.id, "/camera/list", defaultColunms);
    table.setPaginationType("client");
    Camera.table = table.init();
});
