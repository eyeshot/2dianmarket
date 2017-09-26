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
     
    
    <title>两点市场</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="两点市场">
	
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
    <div class="login boxshadow">
        <h2 class="login-title">扫一扫，登录下载您的文件</h2>
        <div id="qrcode" style="width:200px; height:200px; margin-top:15px;margin-left:25px;"></div>
        <div class="login-desc">请使用小程序扫描二维码</div>
    </div>

    <div  id="myDiv" style="width:100px;height: 100px"></div>
</div>


<div class="footer">
    <div class="pull-right">
        <strong>Copyright</strong> © 北京两点网络科技有限公司 <a target="_blank" href="http://www.miitbeian.gov.cn">京ICP备15041503号-1</a>
    </div>
</div>


<script type="text/javascript">
var qrcode = new QRCode(document.getElementById("qrcode"), {
	width : 200,
	height : 200
});

function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};

var genUUID = generateUUID();

function doLogin()
{
    //Ajax调用处理
	$.ajax({
		dataType : "json",
		url : "${contextPath}/web/heart",
		type : "post",
		data : {
			jsvalue : genUUID,
		},
		complete : function(xmlRequest) {
			var returninfo = eval("(" + xmlRequest.responseText + ")");
			if (returninfo.result == 1) {
				//document.location.href = "${contextPath}/web/wxhome?token=" + genUUID;
				document.location.href = "${contextPath}/downlist?token=" + genUUID;
			} 
		}
	});
}

function makeCode () {
	qrcode.makeCode(genUUID);
}

makeCode();

setInterval(doLogin,2000);

</script>

</body>  
</html>
