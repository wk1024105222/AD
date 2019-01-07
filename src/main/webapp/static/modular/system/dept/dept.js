/**
 * 部门管理初始化
 */
var Dept = {
    id: "DeptTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle',width:'50px'},
        {title: '部门简称', field: 'simplename', align: 'center', valign: 'middle', sortable: true},
        // {title: '部门全称', field: 'fullname', align: 'center', valign: 'middle', sortable: true},
        {title: '排序', field: 'num', align: 'center', valign: 'middle', sortable: true},
        //{title: '备注', field: 'tips', align: 'center', valign: 'middle', sortable: true},
        {title: '打卡次数', field: 'attend_times', visible: true, align: 'center', valign: 'middle'},
        {title: '上班时间', field: 'start_work_time', visible: true, align: 'center', valign: 'middle'},
        {title: '下班时间', field: 'end_work_time', visible: true, align: 'center', valign: 'middle'},
        {title: '午休开始', field: 'start_rest_time', visible: true, align: 'center', valign: 'middle'},
        {title: '午休结束', field: 'end_rest_time', visible: true, align: 'center', valign: 'middle'},
        {title: '离岗时间', field: 'leave_time', visible: true, align: 'center', valign: 'middle'},
        {title: '加班时间', field: 'start_over_time', visible: true, align: 'center', valign: 'middle'},
        {title: '邮箱1', field: 'email1', visible: true, align: 'center', valign: 'middle'},
        {title: '邮箱2', field: 'email2', visible: true, align: 'center', valign: 'middle'},
        {title: '邮箱3', field: 'email3', visible: true, align: 'center', valign: 'middle'},
        {title: '发送周期', field: 'send_email_cycle', visible: true, align: 'center', valign: 'middle'},
        {title: '工作日', field: 'work_day', visible: true, align: 'center', valign: 'middle'}

        ];
};

/**
 * 检查是否选中
 */
Dept.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Dept.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加部门
 */
Dept.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加部门',
        area: ['800px', '800px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/dept/dept_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看部门详情
 */
Dept.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '部门详情',
            area: ['800px', '800px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/dept/dept_update/' + Dept.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除部门
 */
Dept.delete = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/dept/delete", function () {
                Feng.success("删除成功!");
                Dept.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("deptId",Dept.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该部门?", operation);
    }
};

/**
 * 查询部门列表
 */
Dept.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Dept.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Dept.initColumn();
    var table = new BSTreeTable(Dept.id, "/dept/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("pid");
    table.setExpandAll(true);
    table.init();
    Dept.table = table;
});
