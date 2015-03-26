<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>

	<body>
		<jsp:include page="module/head/head.jsp"></jsp:include>
		<c:choose>
			<c:when test="${ssiMng == null }">
			</c:when>
			<c:otherwise>
				<jsp:include page="module/search/search.jsp"></jsp:include>
			</c:otherwise>
		</c:choose>
	</body>
</html>
