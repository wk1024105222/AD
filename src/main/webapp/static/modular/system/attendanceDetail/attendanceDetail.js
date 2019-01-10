/**
 * 考勤明细管理初始化
 */
var AttendanceDetail = {
    id: "AttendanceDetailTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AttendanceDetail.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'date', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'type', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
AttendanceDetail.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AttendanceDetail.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加考勤明细
 */
AttendanceDetail.openAddAttendanceDetail = function () {
    var index = layer.open({
        type: 2,
        title: '添加考勤明细',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/attendanceDetail/attendanceDetail_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看考勤明细详情
 */
AttendanceDetail.openAttendanceDetailDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '考勤明细详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/attendanceDetail/attendanceDetail_update/' + AttendanceDetail.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除考勤明细
 */
AttendanceDetail.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/attendanceDetail/delete", function (data) {
            Feng.success("删除成功!");
            AttendanceDetail.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("attendanceDetailId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询考勤明细列表
 */
AttendanceDetail.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    AttendanceDetail.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AttendanceDetail.initColumn();
    var table = new BSTable(AttendanceDetail.id, "/attendanceDetail/list", defaultColunms);
    table.setPaginationType("client");
    AttendanceDetail.table = table.init();
});
