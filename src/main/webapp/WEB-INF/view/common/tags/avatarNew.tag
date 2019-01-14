@/*
    头像参数的说明:
    name : 名称
    id : 头像的id
@*/
<div class="form-group">
    <label class="col-sm-3 control-label head-scu-label">${name}</label>
    <div class="col-sm-4">
        <div id="${id}PreId">
            <div><img id="photo" width="100px" height="100px"
                @if(isEmpty(avatarImg)){
                      src="/static/img/default.jpg"></div>
                @}else{
                      src="${ctxPath}/kaptcha/${avatarImg}"></div>
                @}
        </div>
    </div>
    <div class="col-sm-2">
        <div class="btn-group-vertical">

            <div class="btn btn-sm btn-primary" onclick="openCamera()">&nbsp;拍照</div>
            <div class="btn btn-sm btn-primary" onclick="takePhoto()">&nbsp;确定</div>
            <div class="btn btn-sm btn-primary">&nbsp;关闭</div>

            <script>
                var xmlHttpReq;
                //创建一个XmlHttpRequest对象
                function createXmlHttpRequest() {
                    if(window.XMLHttpRequest) {
                        xmlHttpReq = new XMLHttpRequest();//非IE浏览器
                    }else {
                        xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");//IE浏览器
                    }
                }

                function openCamera() {
                    createXmlHttpRequest();
                    xmlHttpReq.onreadystatechange=openCameraHandle;
                    var url = "/mgr/openCamera";
                    xmlHttpReq.open("get",url,true);
                    xmlHttpReq.send(null);
                }

                function openCameraHandle() {
                    if(xmlHttpReq.readyState==4) {
                        if(xmlHttpReq.status==200) {
                            var res = xmlHttpReq.responseText;
                            Feng.success(res+"摄像头已打开!");
                        }
                    }
                }

                function takePhoto() {
                    createXmlHttpRequest();
                    xmlHttpReq.onreadystatechange=takePhotoHandle;
                    var url = "/mgr/takePhoto";
                    xmlHttpReq.open("get",url,true);
                    xmlHttpReq.send(null);
                }

                function takePhotoHandle() {
                    if(xmlHttpReq.readyState==4) {
                        if(xmlHttpReq.status==200) {
                            var res = xmlHttpReq.responseText;
                            var img=document.getElementById('photo');
                            img.src ="/kaptcha/"+res.substr(1,40);
                            var hidden=document.getElementById("avatar");
                            hidden.value =res.substr(1,40);
                            //img.src ="http://a2.att.hudong.com/74/73/01300543453388144152731911410.jpg";

                            Feng.success(/kaptcha/+res+"拍照成功!");
                        }
                    }
                }

            </script>
        </div>
    </div>
    <div class="col-sm-2 right">

        <div class="upload-btn" id="${id}BtnId">&nbsp;本地</div>

    </div>
    <input type="hidden" id="${id}" value="${avatarImg!}"/>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}


