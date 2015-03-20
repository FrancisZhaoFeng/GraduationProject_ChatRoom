<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'login.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>

	<body>
		<jsp:include page="h-all.jsp"></jsp:include>
		<div align="center">
			<s:form action="manager!login.action">
				<s:textfield label="用户名" name="manager.adminname" /><br/>
				<s:password label="密    码" name="manager.password" /><br/>
				<s:submit value="登陆" />
			</s:form>
		</div>
		<jsp:include page="module/foot/foot.jsp"></jsp:include>
	</body>
</html>
