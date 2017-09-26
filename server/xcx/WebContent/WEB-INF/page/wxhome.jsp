<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/qrcode.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/ajaxfileupload.js"></script>
     
    
    <title>两点市场-上传页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="两点市场-上传页面">
	<link rel="shortcut icon" href="<%=basePath%>/static/img/favicon.ico" />
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/index.css">
	
	<style>
        .footer {
            background: none repeat scroll 0 0 white;
            border-top: 1px solid #e7eaec;
            bottom: 0;
            left: 0;
            padding: 10px 20px;
            position: absolute;
            right: 0;
        }
        .pull-right {
            text-align: center;
        }
    </style>
  </head>
  
  <body>  
  
 <header class="full-container">
    <div class="wrapper">
        <h1>
            <img  src="<%=basePath%>/static/img/logo.jpeg" style="width:55px;height:55px;" alt="">
        </h1>
        <span style="font-size:18px;width:100px; height:70px; line-height:70px; text-align:center;">两点市场</span>
    </div>
</header>



<div id="app">
    <div class="upload boxshadow">
        <div class="user-info clearfix">
            <div class="img">
                <img class="nc-avatar-bg" src="/static/images/avatar-bg.png" alt="">
                <img class="nc-avatar-img" src="${headimg}" alt="">
            </div>
            <div class="info">
                <h3>${nickname}</h3>
            </div>
            <div class="logout">
                <a href="<%=basePath%>upout?token=${token}">退出登录</a>
            </div>
        </div>

        

        <div class="upload-wrapper">
            <input type="hidden" id="token" name="token" value="${token}"> 
            <div class="file-info" align="center">
                <div id="fileName" class="file-item"></div>
                <div id="fileSize" class="file-item"></div>
                <div id="fileType" class="file-item"></div>
            </div>
            
            <div id="wait_loading" style="padding: 50px 0 0 0;display:none;">  
	                                  <div style="width: 10px;height:10px;margin: 0 auto;">
	                                  <img style="height:20px;margin-left:-80px;margin-top:8px;" src="<%=basePath%>/static/img/loading.gif"/></div>  
	                                  <div style="width: 120px;margin: 0 auto;"><span>正在上传...</span></div>  
	        </div> 
	        
	        <div id="show_words" style="padding: 50px 0 0 0;display:none;">  
	                                  <div style="width: 220px;margin: 0 auto;"><span>上传成功,请在小程序中发布代码...</span></div>  
	        </div> 
            
            <div id="uploadinfo">
	            <!-- 添加文件 -->
	            <div class="add-file" >
	                <a class="btn" >
	                    <span>+</span>添加代码
	                    <input type="file" onchange="fileSelected();" name="fileToUpload" id="fileToUpload">
	                </a>
	            </div>
	
	
	            <div class="upload-path">
	                <p>格式：zip，tar，文件大小不超过10MB。</p>
	            </div>
	            <div class="upload-submit" >
	                <button class="btn" onclick="ajaxFileUpload()" >开始上传</button>
	            </div>
            </div>
            
            <div class="upload-submit" id="closepage" style="display:none;">
	                <button class="btn" onclick="loginout()" >安全退出</button>
	        </div>

        </div>

     
    </div>

</div>


<div class="footer">
    <div class="pull-right">
        <strong>Copyright</strong> © 北京两点网络科技有限公司 <a target="_blank" href="http://www.miitbeian.gov.cn">京ICP备15041503号-1</a>
    </div>
</div>


<script type="text/javascript">

function fileSelected()
{
    var file = document.getElementById('fileToUpload').files[0];
    if (file) {
        var fileSize = 0;
        if (file.size > 1024 * 1024)
            fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
        else
            fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

        document.getElementById('fileName').innerHTML = '名称: ' + file.name;
        document.getElementById('fileSize').innerHTML = '大小: ' + fileSize;
        document.getElementById('fileType').innerHTML = '类型: ' + file.type;
    }
}

function loginout()
{
	document.location.href = "https://www.2dian.com/xcx/web/loginout?token=" + document.getElementById('token').value;
}


function ajaxFileUpload() {  
	var file = document.getElementById('fileToUpload').files[0];
	if(!file)
	{
		alert("请先添加代码!");
		return;
	}
	if(file.type.indexOf("zip") !=-1  || file.type.indexOf("tar") != -1)
	{
		// 开始上传文件时显示一个图片  
	    $("#wait_loading").show();      
	    $("#uploadinfo").hide();
	    var loginToken = document.getElementById('token').value;
	    $.ajaxFileUpload({  
	     url: 'https://www.2dian.com/xcx/web/upload',   
	     type: 'post',  
	     secureuri: false, //一般设置为false  
	     fileElementId: 'fileToUpload', // 上传文件的id、name属性名  
	     dataType: 'JSON', //返回值类型，一般设置为JSON、application/json , 注意这里必须是大写 
	     data:{token:loginToken},//附加参数，json格式  
	     success: function(data,status)
	     {   
	         if(data.msg == 'success'){  
	           $("#wait_loading").hide();   
	            // alert("上传成功！");  
	            $("#show_words").show();
	            $("#closepage").show();
	         }else{  
	           $("#wait_loading").hide();  
	           $("#uploadinfo").show();
	          alert("上传失败,请重新上传!");  
	         }  
	     },  
	     error: function(data, status, e){   
	           $("#wait_loading").hide();  
	          alert("请求失败！");  
	     }  
	    });  
	} else 
	{
		alert("文件格式只能是zip或tar!");
		return;
	}
    
} 

</script>

</body>  
</html>
