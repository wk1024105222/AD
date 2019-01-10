@/*
    头像参数的说明:
    name : 名称
    id : 头像的id
@*/
<div class="form-group">
    <label class="col-sm-3 control-label head-scu-label">${name}</label>
    <div class="col-sm-4">
        <div id="${id}PreId">
            <div><img width="100px" height="100px"
                @if(isEmpty(avatarImg)){
                      src="${ctxPath}/static/img/default.jpg"></div>
                @}else{
                      src="${ctxPath}/kaptcha/${avatarImg}"></div>
                @}
        </div>
    </div>@/*
    <div class="btn-group-vertical" >
        <button class="btn btn-default btn-sm btn-danger" >&nbsp;拍照</button>
        <button class="btn btn-default btn-sm btn-warning">&nbsp;确定</button>
        <button class="btn btn-default btn-sm btn-primary" >&nbsp;本地</button>
    </div>@*/
    <div class="col-sm-2">
        <div class="head-scu-btn upload-btn" id="${id}BtnId">
            <i class="fa fa-upload"></i>&nbsp;上传
        </div>
    </div>
    <input type="hidden" id="${id}" value="${avatarImg!}"/>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}


