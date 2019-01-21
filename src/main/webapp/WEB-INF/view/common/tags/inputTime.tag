
<div class="form-group">
    <label class="col-sm-3 control-label">${name}</label>
    <div class="col-sm-9">
        <input class="form-control" id="${id}" name="${id}" readonly
               @if(isNotEmpty(value)){
               value="${tool.dateType(value)}"
               @}
        >
        <script type="text/javascript">
            $("#${id}").datetimepicker({
                format: '${format}',  //显示格式可为yyyymm/yyyy-mm-dd/yyyy.mm.dd
                weekStart: 1,  	//0-周日,6-周六 。默认为0
                autoclose: true,
                startView: ${startView},  	//打开时显示的视图。0-'hour' 1-'day' 2-'month' 3-'year' 4-'decade'
                minView: ${minView},  	//最小时间视图。默认0-'hour'
                initialDate: new Date(),	//初始化日期.默认new Date()当前日期
                forceParse: false,  	//当输入非格式化日期时，强制格式化。默认true
                bootcssVer:3,	//显示向左向右的箭头
                language: 'zh-CN', //语言
                minuteStep: ${minuteStep}
            });
        </script>
    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
<div class="hr-line-dashed"></div>
@}


