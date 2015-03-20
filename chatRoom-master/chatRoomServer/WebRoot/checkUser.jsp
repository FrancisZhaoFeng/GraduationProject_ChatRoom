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
			List<com.zhbit.crs.domain.User> users = (List<com.zhbit.crs.domain.User>) session.getAttribute("Users");
			rowCount = users.size();
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;
			} else {
				pageCount = rowCount / pageSize + 1;
			}
			List<com.zhbit.crs.domain.User> listUsers = new ArrayList<com.zhbit.crs.domain.User>();
			int firstNum = (pageNow - 1) * pageSize;
			for (int i = 0; i < pageSize; i++) {
				if(firstNum + i == users.size())break;
				listUsers.add((com.zhbit.crs.domain.User) users.get(firstNum + i));
			}
			request.setAttribute("listUsers", listUsers);
		%>
		<form method="post" action="settleAccount.jsp" name="form">
			<table style="margin: 0px auto" width="960px"  border="0"
				bordercolor="#000" cellpadding="0" cellspacing="0">
				<tr>
					<td width="5%">
						<div align="center">
							用户id
						</div>
					</td>
					<td width="8%">
						<div align="center" style="height:30px;">
							用户名
						</div>
					</td>
					<td width="8%">
						<div align="center">
							电话
						</div>
					</td>
					<td width="5%">
						<div align="center">
							年龄
						</div>
					</td>
					<td width="5%">
						<div align="center">
							性别
						</div>
					</td>
					<td width="5%">
						<div align="center">
							在线
						</div>
					</td>
					<td width="5%">
						<div align="center">
							黑名单
						</div>
					</td>
				</tr>
				<s:iterator value="#request.listUsers" var="user">
					<tr>
						<td width="5%">
							<div align="center">
								<s:property value="#user.userid" />
							</div>
						</td>
						<td width="8%">
							<div align="center">
								<s:property value="#user.username" />
							</div>
						</td>
						<td width="8%">
							<div align="center">
								<s:property value="#user.telephone" />
							</div>
						</td>
						<td width="5%">
							<div align="center">
								<s:property value="#user.age" />
							</div>
						</td>
						<td width="5%">
							<div align="center">
								<s:if test="#user.sex == 1">男</s:if>
								<s:else>女</s:else>
							</div>
						</td>
						<td width="5%">
							<div align="center">
								<s:if test="#user.online == 1">在线</s:if>
								<s:else>不在线</s:else>
							</div>
						</td>
						<td width="140px">
							<div align="center">
								<button type="button"
									onclick="updateUserBL('<s:property value="#user.userid" />','<s:property value="#user.username" />')">
									<s:if test="#user.blacklist == 1">恢复</s:if>
									<s:else><div style="color: red">限制</div></s:else>
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
