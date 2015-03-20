<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<link rel="stylesheet" href="module/navigation-bar/css/style.css" type="text/css" media="screen, projection"/>
	<script type='text/javascript' src='module/navigation-bar/js/jquery.min.js'></script>
	<script type="text/javascript" language="javascript" src="module/navigation-bar/js/jquery.dropdown.js"></script>
  </head>
  
  <body>
		<div style="display: block;height:" id="page-wrap">

			<ul class="dropdown">
				<li>
					<a href="book!selectBookByAcademy?book.academy=信院">信院</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=测控技术与仪器">测控技术与仪器</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=电气工程及其自动化">电气工程及其自动化</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=电子科学与技术">电子科学与技术</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=通信工程">通信工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=信息工程">信息工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=自动化">自动化</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=计院">计院</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=计算机科学与技术">计算机科学与技术</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=软件工程">软件工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=数字媒体技术">数字媒体技术</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=网络工程">网络工程</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=机车">机车</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=车辆工程">车辆工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=机械电子工程">机械电子工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=机械工程">机械工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=交通工程">交通工程</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=化院">化院</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=材料科学与工程">材料科学与工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=化学工程与工艺">化学工程与工艺</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=环境工程">环境工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=生物工程">生物工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=食品科学与工程">食品科学与工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=应用化学">应用化学</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=外院">外院</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=日语">日语</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=商务英语">商务英语</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=英语">英语</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=数院">数院</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=应用统计学">应用统计学</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=应用物理学">应用物理学</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=商院">商院</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=工程管理">工程管理</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=公共事业管理">公共事业管理</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=国际经济与贸易">国际经济与贸易</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=市场营销">市场营销</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=信息管理与信息系统">信息管理与信息系统</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=会计">会计</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=财务管理">财务管理</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=会计学">会计学</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=信用管理">信用管理</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=文法">文法</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=法学">法学</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=设计">设计</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=产品设计">产品设计</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=服装设计与工程">服装设计与工程</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=工艺美术">工艺美术</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=环境设计">环境设计</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=视觉传达设计">视觉传达设计</a>
						</li>
					</ul>
				</li>
				<li>
					<a href="book!selectBookByAcademy?book.academy=课外">课外</a>
					<ul class="sub_menu">
						<li>
							<a href="book!selectBookByMajor?book.major=通用考试">通用考试</a>
						</li>
						<li>
							<a href="book!selectBookByMajor?book.major=文学书籍">文学书籍</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</body>
</html>
