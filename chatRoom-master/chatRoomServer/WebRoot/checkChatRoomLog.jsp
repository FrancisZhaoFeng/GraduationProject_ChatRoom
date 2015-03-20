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
	<body>
		<%
			//定义分页用到的变量
			int pageSize = 18;
			int pageNow = 1;//默认第一页
			int rowCount = 0;//该值需从数据库中查询
			int pageCount = 0;//该值需通过pageSize和rowCount计算得出
			//接收用户希望显示的页数（pageNow）
			String s_pageNow = request.getParameter("pageNow");
			if (s_pageNow != null && !s_pageNow.equals("")) {
				System.out.println("=======*" + s_pageNow + "=======");
				pageNow = Integer.parseInt(s_pageNow);
			}
			//取出session中的值
			List<com.zhbit.crs.domain.ChatRoomLog> chatRoomLogs = (List<com.zhbit.crs.domain.ChatRoomLog>) session.getAttribute("ChatRoomLogs");
			rowCount = chatRoomLogs.size();
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;
			} else {
				pageCount = rowCount / pageSize + 1;
			}
			List<com.zhbit.crs.domain.ChatRoomLog> listChatRoomLogs = new ArrayList<com.zhbit.crs.domain.ChatRoomLog>();
			int firstNum = (pageNow - 1) * pageSize;
			for (int i = 0; listChatRoomLogs.size() != pageSize; i++) {
				if (firstNum + i == chatRoomLogs.size())
					break;
				if(chatRoomLogs.get(firstNum + i).getType() != 4)
					listChatRoomLogs.add((com.zhbit.crs.domain.ChatRoomLog) chatRoomLogs.get(firstNum + i));
			}
			request.setAttribute("listChatRoomLogs", listChatRoomLogs);
		%>
		<form method="post" action="settleAccount.jsp" name="form">
			<table style="margin: 0px auto" width="960px" border="0"
				bordercolor="#000" cellpadding="0" cellspacing="0">
				<tr>
					<td width="5%">
						<div align="center">
							聊天记录id
						</div>
					</td>
					<td width="8%">
						<div align="center">
							发送者名字
						</div>
					</td>
					<td width="8%">
						<div align="center">
							聊天室名字
						</div>
					</td>
					<td width="5%">
						<div align="center">
							时间
						</div>
					</td>
					<td width="16%">
						<div align="center">
							内容
						</div>
					</td>
					<td width="5%">
						<div align="center">
							操作
						</div>
					</td>
				</tr>
				<s:iterator value="#request.listChatRoomLogs" var="chatRoomLog">
					<tr>
						<td width="5%">
							<div align="center">
								<s:property value="#chatRoomLog.logid" />
							</div>
						</td>
						<td width="8%">
							<div align="center">
								<s:property value="#chatRoomLog.user.username" />
							</div>
						</td>
						<td width="8%">
							<div align="center">
								<s:property value="#chatRoomLog.chatRoom.chatroomname" />
							</div>
						</td>
						<td width="16%">
							<div align="center">
								<!--<s:property value="#chatRoomLog.sendtime" />-->
								<fmt:formatDate value="${chatRoomLog.sendtime}" pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
						</td>
						<td width="32%" >
							<div align="left" style="overflow:auto; height: 30px;padding-top: 13px;">
								<s:if test="#chatRoomLog.type == 1">
									<s:property value="#chatRoomLog.sendtext" />
								</s:if>
								<s:elseif test="#chatRoomLog.type == 2">
									<s:property value="#chatRoomLog.sendimage" />
								</s:elseif>
								<s:elseif test="#chatRoomLog.type == 3">
									<s:property value="#chatRoomLog.sendvoice" />
								</s:elseif>
							</div>
						</td>
						<td width="140px">
							<div align="center">
								<button style="color: red" type="button"
									onclick="updateChatRoomLog('<s:property value="#chatRoomLog.logid" />')">
									删除
								</button>
							</div>
						</td>
				</s:iterator>
			</table>
		</form>
		<div align="center" style="margin-top: 6px">
			<%
				//上一页
				if (pageNow != 1) {
					out.println("<a href = checkDisplay.jsp?pageNow=" + (pageNow - 1) + ">上一页</a>");
				}
				for (int i = 1; i <= pageCount; i++) {
					out.println("<a href = checkDisplay.jsp?pageNow=" + i + ">[" + i + "]</a>");
				}
				//下一页
				if (pageNow != pageCount) {
					out.println("<a href = checkDisplay.jsp?pageNow=" + (pageNow + 1) + ">下一页</a>");
				}
			%>
		</div>
	</body>
</html>
