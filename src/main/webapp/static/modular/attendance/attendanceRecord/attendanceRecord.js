/**
 * 出入记录管理初始化
 */
var AttendanceRecord = {
    id: "AttendanceRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AttendanceRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'uuid', visible: true, align: 'center', valign: 'middle'},
            {title: '登记时间', field: 'attendanceTime', visible: true, align: 'center', valign: 'middle'},
            {title: '工号', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '姓名', field: 'userName', visible: true, align: 'center', valign: 'middle'},
            {title: '动作', field: 'actionName', visible: true, align: 'center', valign: 'middle'},
            // {title: '照片存储路径', field: 'imgPath', visible: true, align: 'center', valign: 'middle'},
            {title: '公司', field: 'company', visible: true, align: 'center', valign: 'middle'},
            {title: '部门', field: 'department', visible: true, align: 'center', valign: 'middle'},
            {title: '组', field: 'team', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'note', visible: true, align: 'center', valign: 'middle',width:300}
    ];
};

/**
 * 检查是否选中
 */
AttendanceRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AttendanceRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加出入记录
 */
AttendanceRecord.openAddAttendanceRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加出入记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/attendanceRecord/attendanceRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看出入记录详情
 */
AttendanceRecord.openAttendanceRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '出入记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/attendanceRecord/attendanceRecord_update/' + AttendanceRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除出入记录
 */
AttendanceRecord.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/attendanceRecord/delete", function (data) {
            Feng.success("删除成功!");
            AttendanceRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("attendanceRecordId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询出入记录列表
 */
AttendanceRecord.search = function () {
    var queryData = {};
    queryData['user'] = $("#user").val();
    queryData['date'] = $("#date").val();
    AttendanceRecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AttendanceRecord.initColumn();
    var table = new BSTable(AttendanceRecord.id, "/attendanceRecord/list", defaultColunms);
    table.setPaginationType("client");
    AttendanceRecord.table = table.init();
});
