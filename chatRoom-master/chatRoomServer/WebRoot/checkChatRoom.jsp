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
			List<com.zhbit.crs.domain.ChatRoom> chatRooms = (List<com.zhbit.crs.domain.ChatRoom>) session.getAttribute("ChatRooms");
			rowCount = chatRooms.size();
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;
			} else {
				pageCount = rowCount / pageSize + 1;
			}
			List<com.zhbit.crs.domain.ChatRoom> listChatRooms = new ArrayList<com.zhbit.crs.domain.ChatRoom>();
			int firstNum = (pageNow - 1) * pageSize;
			for (int i = 0; listChatRooms.size() != pageSize; i++) {
				if(firstNum + i == chatRooms.size())break;
				if(!chatRooms.get(firstNum + i).getIsdelete())
					listChatRooms.add((com.zhbit.crs.domain.ChatRoom)chatRooms.get(firstNum + i));
			}
			request.setAttribute("listChatRooms", listChatRooms);
		%>
		<form method="post" action="settleAccount.jsp" name="form">
			<table style="margin: 0px auto" width="960px"  border="0"
				bordercolor="#000" cellpadding="0" cellspacing="0">
				<tr>
					<td width="5%">
						<div align="center">
							聊天室id
						</div>
					</td>
					<td width="8%">
						<div align="center">
							聊天室名字
						</div>
					</td>
					<td width="8%">
						<div align="center">
							备注
						</div>
					</td>
					<td width="5%">
						<div align="center">
							创建者
						</div>
					</td>
					<td width="5%">
						<div align="center">
							操作
						</div>
					</td>
				</tr>
				<s:iterator value="#request.listChatRooms" var="chatRoom">
					<tr>
						<td width="5%">
							<div align="center" style="height:30px;">
								<s:property value="#chatRoom.chatroomid" />
							</div>
						</td>
						<td width="8%">
							<div align="center" >
								<s:property value="#chatRoom.chatroomname" />
							</div>
						</td>
						<td width="8%">
							<div align="center" style="overflow:auto; height: 30px;">
								<s:property value="#chatRoom.note" />
							</div>
						</td>
						<td width="5%">
							<div align="center">
								<s:property value="#chatRoom.user.username" />
							</div>
						</td>
						<td width="140px">
							<div align="center">
								<button style="color: red" type="button" onclick="updateChatRoom('<s:property value="#chatRoom.chatroomid" />')">
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
