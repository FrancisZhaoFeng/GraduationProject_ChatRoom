<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<script language="javascript">
    		function login(checkPage){
    		  var mmgName = "<%=session.getAttribute("ssiMngName")%>";
    		  name = new String(mmgName);
    		  //alert("mmgName:"+mmgName);
    		  //alert("name:"+name);
    		  if(name == "null"){
    		  //if(typeof(mmgName) != "undefined"){
    		   	alert("请登录聊天管理系统:"+mmgName);
    		   	//window.location.href= checkPage; 
    		   	window.location.href= "login.jsp";
    		  	//document.getElementById("checked").href = "login.jsp";
    		  }else{
    		  	//alert("账号名:"+mmgName);
    		  	window.location.href= checkPage;
    		  	//return "false.jsp";
    		  	//document.getElementById("checked").href = "false.jsp";
    		  }
    		} 
    	</script>
		<base href="<%=basePath%>">

		<title>My JSP 'header.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="module/head/head.css">
	</head>

	<body>
		<div class="site-topbar">
			<div style="width:960px;margin: 0px auto" class="contain">
				<div class="topbar-left">
					<a href="#">聊天室管理系统</a>
					<span class="sep">|</span>
					<!--<a href="javascript:login('checkUser.jsp');">查看用户</a>-->
					<a href="javascript:login('manager!selectUser.action');">查看用户</a>
					<span class="sep">|</span>
					<a href="javascript:login('manager!selectChatRoom.action');">查看聊天室</a>
					<span class="sep">|</span>
					<a href="javascript:login('manager!selectChatRoomLog.action');">查看聊天室聊天记录</a>
					<span class="sep">|</span>
					<a href="javascript:login('manager!selectChatPerLog.action');">查看好友间聊天记录</a>
					<span class="sep">|</span>
					<a href="http://www.meizu.com">待定</a>
				</div>
				<div class="topbar-right">
					<c:choose>
						<c:when test="${ssiMng == null }">
						[<a href="register.jsp"><font color="#000000">管理员免费注册（M码）</font> </a>]
						[<a href="login.jsp"><font color="#000000">登录</font> </a>]
					</c:when>
						<c:otherwise>
						欢迎您：${ssiMngName} 
						[<a href="changePwd.jsp"><font color="#000000">修改密码</font> </a>]  
						[<a href="manager!exitLogin.action?exit=true"><font color="#000000">退出登录</font> </a>]
					</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</body>
</html>
