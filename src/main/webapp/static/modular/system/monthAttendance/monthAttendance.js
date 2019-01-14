/**
 * 月度考勤表管理初始化
 */
var MonthAttendance = {
    id: "MonthAttendanceTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MonthAttendance.initColumn = function () {
    return [
         {field: 'selectItem', radio: true},
        //     {title: '', field: 'uuid', visible: true, align: 'center', valign: 'middle'},
        {title: '年份', field: 'year', visible: true, align: 'center', valign: 'middle',width:50},
        {title: '月份', field: 'month', visible: true, align: 'center', valign: 'middle',width:50},
        {title: '天数', field: 'days', visible: true, align: 'center', valign: 'middle',width:50},
        {title: '工号', field: 'userId', visible: true, align: 'center', valign: 'middle',width:100},
        {title: '姓名', field: 'userName', visible: true, align: 'center', valign: 'middle',width:100},
        {title: '公司', field: 'company', visible: true, align: 'center', valign: 'middle',width:160},
        {title: '部门', field: 'department', visible: true, align: 'center', valign: 'middle',width:120},
        {title: '组', field: 'team', visible: true, align: 'center', valign: 'middle',width:100},
        {title: '1', field: 'day1', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '2', field: 'day2', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '3', field: 'day3', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '4', field: 'day4', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '5', field: 'day5', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '6', field: 'day6', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '7', field: 'day7', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '8', field: 'day8', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '9', field: 'day9', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '10', field: 'day10', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '11', field: 'day11', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '12', field: 'day12', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '13', field: 'day13', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '14', field: 'day14', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '15', field: 'day15', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '16', field: 'day16', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '17', field: 'day17', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '18', field: 'day18', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '19', field: 'day19', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '20', field: 'day20', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '21', field: 'day21', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '22', field: 'day22', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '23', field: 'day23', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '24', field: 'day24', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '25', field: 'day25', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '26', field: 'day26', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '27', field: 'day27', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '28', field: 'day28', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '29', field: 'day29', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '30', field: 'day30', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}},
        {title: '31', field: 'day31', visible: true, align: 'center', valign: 'middle',width:150,
            cellStyle:{css:{"word-break": "normal","word-wrap":"break-word","white-space":"pre-wrap"}	}}
    ];
};

/**
 * 检查是否选中
 */
MonthAttendance.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MonthAttendance.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加月度考勤表
 */
MonthAttendance.openAddMonthAttendance = function () {
    var index = layer.open({
        type: 2,
        title: '添加月度考勤表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/monthAttendance/monthAttendance_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看月度考勤表详情
 */
MonthAttendance.openMonthAttendanceDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '月度考勤表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/monthAttendance/monthAttendance_update/' + MonthAttendance.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除月度考勤表
 */
MonthAttendance.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/monthAttendance/delete", function (data) {
            Feng.success("删除成功!");
            MonthAttendance.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("monthAttendanceId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询月度考勤表列表
 */
MonthAttendance.search = function () {
    var queryData = {};
    queryData['user'] = $("#user").val();
    queryData['year'] = $("#year").val();
    queryData['month'] = $("#month").val();
    MonthAttendance.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MonthAttendance.initColumn();
    var table = new BSTable(MonthAttendance.id, "/monthAttendance/list", defaultColunms);
    table.setPaginationType("client");
    // table.setClasses("");
    MonthAttendance.table = table.init();
});
