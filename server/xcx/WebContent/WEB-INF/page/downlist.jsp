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
                <a href="<%=basePath%>downout?token=${token}">退出登录</a>
            </div>
        </div>
        

        <div class="upload-wrapper">
            <input type="hidden" id="token" name="token" value="${token}"> 
            <ul style="margin-top:10px;border:2px solid #eee;">
            <c:forEach items="${map}" var="node">  
               <li style="border:1px solid #eee;height:50px;line-height:50px;font-size:16px;color:black;margin-left:10px;">
               <a href="<%=basePath%>/download?key=${node.key}&token=${token}"><c:out value="${node.value}"></c:out></a>
               </li>
            </c:forEach> 
            </ul>
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


</script>

</body>  
</html>
