/**
 * 影像管理管理初始化
 */
var ImageChange = {
    id: "ImageChangeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ImageChange.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '工号', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '照片', field: 'imgName', visible: true, align: 'center', valign: 'middle'},
            {title: '操作', field: 'actionType', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '发送状态', field: 'sendFlag', visible: true, align: 'center', valign: 'middle'},
            {title: '返回状态', field: 'receiveFlag', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            {title: '引擎id', field: 'imageId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ImageChange.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ImageChange.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加影像管理
 */
ImageChange.openAddImageChange = function () {
    var index = layer.open({
        type: 2,
        title: '添加影像管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/imageChange/imageChange_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看影像管理详情
 */
ImageChange.openImageChangeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '影像管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/imageChange/imageChange_update/' + ImageChange.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除影像管理
 */
ImageChange.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/imageChange/delete", function (data) {
            Feng.success("删除成功!");
            ImageChange.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("imageChangeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询影像管理列表
 */
ImageChange.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ImageChange.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ImageChange.initColumn();
    var table = new BSTable(ImageChange.id, "/imageChange/list", defaultColunms);
    table.setPaginationType("client");
    ImageChange.table = table.init();
});
