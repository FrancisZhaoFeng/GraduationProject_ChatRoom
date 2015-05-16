<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'false.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="js/js.js" charset="utf-8"></script>

</head>
<%
	int numPage = 0;
	String pageCheck = (String) session.getAttribute("pageCheck") + ".jsp";
	String pageCheckUser = (String) session.getAttribute("pageCheckUser") + ".jsp";
	String pageCheckChatRoom = (String) session.getAttribute("pageCheckChatRoom") + ".jsp";
	String pageCheckChatRoomLog = (String) session.getAttribute("pageCheckChatRoomLog") + ".jsp";
	String pageCheckChatPerLog = (String) session.getAttribute("pageCheckChatPerLog") + ".jsp"; //pageCheckChatPerLog
	System.out.println("*==*:" + pageCheck + "-" + "-" + pageCheckUser + "-" + pageCheckChatRoom + "-" + pageCheckChatRoomLog + "-" + pageCheckChatPerLog);
%>
<body>
	<jsp:include page="h-all.jsp"></jsp:include>
	<label> ${message} </label>
	<s:if test="#session.SearchStr!=null">
		<%
			System.out.println("======session.SearchStr!=null===:" + session.getAttribute("SearchStr") + "-" + session.getAttribute("pageCheckUser"));
		%>
		<s:if test="#session.pageCheckUser!=null">
			<jsp:include page="<%=pageCheckUser%>"></jsp:include>
			<hr style="margin: 18px auto; width: 960px;" />
		</s:if>
		<s:if test="#session.pageCheckChatRoom!=null">
			<jsp:include page="<%=pageCheckChatRoom%>"></jsp:include>
			<hr style="margin: 18px auto; width: 960px;" />
		</s:if>
		<s:if test="#session.pageCheckChatRoomLog!=null">
			<jsp:include page="<%=pageCheckChatRoomLog%>"></jsp:include>
			<hr style="margin: 18px auto; width: 960px;" />
		</s:if>
		<s:if test="#session.pageCheckChatPerLog!=null">
			<jsp:include page="<%=pageCheckChatPerLog%>"></jsp:include>
			<hr style="margin: 18px auto; width: 960px;" />
		</s:if>
		<s:if
			test="#session.pageCheckUser==null && #session.pageCheckChatRoom==null && #session.pageCheckChatRoomLog==null && #session.pageCheckChatPerLog==null ">
			<div align="center"
				style="margin: 0px auto; font-size: medium; color: red">
				搜索结果为空</div>
		</s:if>
	</s:if>
	<s:else>
		<%
			System.out.println("======session.SearchStr==null===:" + session.getAttribute("SearchStr"));
		%>
		<jsp:include page="<%=pageCheck%>"></jsp:include>
	</s:else>

	<jsp:include page="module/foot/foot.jsp"></jsp:include>
</body>
</html>
