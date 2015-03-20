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
    
    <title>My JSP 'false.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
<style>
.miaoshu{
border: ridge;
padding: 8px;
width: 960px;
/* background-color: rgb(244, 244, 108); */
margin-right: auto;
margin-left: auto;
margin-top: 10px;
font-size: 18px;
}


</style>    

  
  <body>
  		<jsp:include page="h-all.jsp"></jsp:include>
		<div class="miaoshu">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;近年来，越来越多的企业在其内部使用局域网来进行工作。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在内部局域网的帮助下，企业得以简化信息流程，提高信息交换的速度，从而提高工作效率。然而，随着企业规模的扩大，业务量的增加，企业内部的信息越来越私密，企业只希望员工通过内部局域网进行沟通与交流，避免企业内部机密通过Internet泄露到外部及表达情感的交流工具。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了解决上述矛盾，人们提出了开发局域网聊天软件的构想，通过局域网聊天软件，企业员工可以随时的进行即时消息传递，召开网络会议等，有利于提高工作效率，同时又保护了企业内部信息的安全。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;随着用户对软件功能的需求不断提高,即时通讯的产品也不断地更新换代。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;即时通讯的发展不论是在基础应用方面还是在扩展应用方面都有着飞跃。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了满足用户的需求，包括腾讯公司的聊天软件QQ在内，许多即时性聊天工具都推出了语音聊天功能模块。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;基于上述市场需求，本毕业设计采用Android开发一款基于局域网的聊天室应用系统，实现一般的文字聊天功能和语音聊天功能等，可作为某局域网的交流工具。<br/>
			<h1 style="font-size: 18px;">研究目标：</h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本毕业设计是基于Android的局域网聊天室，用Android编写的程序，程序实现一般的文字聊天，语音聊天，图片发送等功能，可作为某局域网的交流工具。<br/>
			<h1 style="font-size: 18px;">研究内容：</h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在本毕业设计中，局域网的文字语音通信通过Socket编程实现，Android封装了有关Socket的各种操作，在编程过程中调用封装的函数实现各种功能。基于TCP的Socket编程采用流式套接字。<br/>
			<h1 style="font-size: 18px;">服务端-管理员：</h1>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.登陆：系统管理员登陆，账号和密码预先写入mysql数据库。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.查看在线人数：系统管理员可以查看当前登陆系统的在线人数。<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.用户权限设定：管理员可设置用户的权限，包括禁止发送任何消息，恢复发送消息权限。<br/>
		</div>
		<jsp:include page="module/foot/foot.jsp"></jsp:include>
	</body>
</html>
