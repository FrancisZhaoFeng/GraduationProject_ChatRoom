<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><!--
   <link href="module/search/searchfield/searchfield.css" rel="stylesheet" type="text/css" media="screen" />
-->
<!-- end -->
<script src="/mint/mint_v129/?js" type="text/javascript"></script>

</head>
<body>
	<div style="width:960px;margin: 10px auto;display:block;height:50px;" align="right">
			<form id="searchform" action="manager!searchStr.action" method="post">
				<p>
					<input type="text" id="searchfield" name="searchStr" value="" />
					<button type="submit" id="searcbutton">
						搜索
					</button>
				</p>
			</form>
		</div>
</body>
</html>
