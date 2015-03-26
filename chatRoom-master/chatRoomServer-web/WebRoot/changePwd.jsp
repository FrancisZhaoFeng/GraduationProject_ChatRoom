<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'changePwd.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		<jsp:include page="h-all.jsp"></jsp:include>
		<div align="center">
			<label>
				${message}
			</label>
			<s:form action="manager!updateManager.action">
				<s:password label="输入旧密码" name="oldpwd" />
				<s:password label="密码新密码" name="manager.password" />
				<s:password label="再次输入密码" name="repwd" />
				<s:submit value="提 交" />
			</s:form>
		</div>
		<jsp:include page="module/foot/foot.jsp"></jsp:include>
	</body>
</html>
