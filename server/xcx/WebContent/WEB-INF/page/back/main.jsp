<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>后台管理系统</title>
		<link rel="stylesheet" type="text/css" href="${contextPath}/static/ext/examples/portal/portal.css" />
		<link rel="stylesheet" type="text/css" href="${contextPath}/static/ext/examples/shared/example.css" />
		<link rel="stylesheet" type="text/css" href="${contextPath}/static/css/forestry-style.css"  />
	    <script type="text/javascript" src="${contextPath}/static/ext/examples/shared/include-ext.js"></script>
	    <script type="text/javascript" src="${contextPath}/static/ext/locale/ext-lang-zh_CN.js"></script>
	    <script type="text/javascript" src="${contextPath}/static/ext/examples/shared/options-toolbar.js"></script>
	    <script type="text/javascript" src="${contextPath}/static/ext/examples/shared/examples.js"></script>
	    <script type="text/javascript">
	  		var userName = '${SESSION_SYS_USER.userName}';
	  		var globalRoleId = '${SESSION_SYS_USER.role}';
	    	var appBaseUri = '${contextPath}';
	    	var appName = '后台管理系统';
	        Ext.Loader.setPath('Ext.app', '${contextPath}/static/ext/examples/portal/classes');
	        Ext.Loader.setPath('Ext.ux', '${contextPath}/static/ext/examples/ux');
	        Ext.Loader.setPath('Forestry.app', '${contextPath}/static/ext/examples/portal');
	    </script>
	    <script type="text/javascript" src="${contextPath}/static/ext/examples/portal/portal.js"></script>
	    <script type="text/javascript">
	        Ext.require([
	            'Ext.layout.container.*',
	            'Ext.resizer.Splitter',
	            'Ext.fx.target.Element',
	            'Ext.fx.target.Component',
	            'Ext.window.Window',
	            'Ext.app.Portlet',
	            'Ext.app.PortalColumn',
	            'Ext.app.PortalPanel',
	            'Ext.app.Portlet',
	            'Ext.app.PortalDropZone'
	        ]);
	     	// alert("<spring:message code='g_operateFailure' />");
	    </script>
	</head>
	<body>
		<span id="app-msg" style="display:none;"></span>
	    <form id="history-form" class="x-hide-display">
	        <input type="hidden" id="x-history-field" />
	        <iframe id="x-history-frame"></iframe>
	    </form>
	</body>
</html>
