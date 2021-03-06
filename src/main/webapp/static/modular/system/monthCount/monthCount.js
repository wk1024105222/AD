/**
 * 月度统计表管理初始化
 */
var MonthCount = {
    id: "MonthCountTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MonthCount.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '月份', field: 'month', visible: true, align: 'center', valign: 'middle',width:80},
            {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle',width:120},
            {title: '工号', field: 'userId', visible: true, align: 'center', valign: 'middle',width:100},
            {title: '姓名', field: 'userName', visible: true, align: 'center', valign: 'middle',width:100},
            {title: '公司', field: 'company', visible: true, align: 'center', valign: 'middle',width:160},
            {title: '部门', field: 'department', visible: true, align: 'center', valign: 'middle',width:120},
            {title: '组', field: 'team', visible: true, align: 'center', valign: 'middle',width:100},
            {title: '次数', field: 'times', visible: true, align: 'center', valign: 'middle',width:60},
            {title: '日期', field: 'dates', visible: true, align: 'center', valign: 'middle',
                cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}}
    ];
};

/**
 * 检查是否选中
 */
MonthCount.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MonthCount.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加月度统计表
 */
MonthCount.openAddMonthCount = function () {
    var index = layer.open({
        type: 2,
        title: '添加月度统计表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/monthCount/monthCount_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看月度统计表详情
 */
MonthCount.openMonthCountDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '月度统计表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/monthCount/monthCount_update/' + MonthCount.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除月度统计表
 */
MonthCount.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/monthCount/delete", function (data) {
            Feng.success("删除成功!");
            MonthCount.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("monthCountId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 导出月度统计表列表
 */
MonthCount.export = function () {
    // var request=new XMLHttpRequest();
    // request.open("POST",Feng.ctxPath + "/monthCount/export");
    // request.setRequestHeader("Content-type","text/plain;charset=UTF-8");
    // request.send({"user":$("#user").val(),"month":$("#month").val(),"typeFlag":$("#typeFlag").val()});


    // var ajax = new $ax(Feng.ctxPath + "/monthCount/export");
    // ajax.set("user",$("#user").val());
    // ajax.set("month",$("#month").val());
    // ajax.set("typeFlag",$("#typeFlag").val());
    // ajax.start();

    window.location.href=Feng.ctxPath + "/monthCount/export"+"?" +
        "user="+ $("#user").val()+
        "&month="+$("#month").val()+
        "&typeFlag="+$("#typeFlag").val();

};

/**
 * 查询月度统计表列表
 */
MonthCount.search = function () {
    var queryData = {};
    queryData['user'] = $("#user").val();
    queryData['month'] = $("#month").val();
    queryData['typeFlag'] = $("#typeFlag").val();
    MonthCount.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MonthCount.initColumn();
    var table = new BSTable(MonthCount.id, "/monthCount/list", defaultColunms);
    table.setPaginationType("client");
    MonthCount.table = table.init();
});
