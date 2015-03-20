<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <body>
  <jsp:include page="h-all.jsp"></jsp:include>
    <div align="center">
    	<label>
			${message}
		</label>
    	<s:form action="manager!register.action" >
			<s:textfield label="账号 *" name="manager.adminname" />
			<s:password label="密码 *" name="manager.password" />
			<s:password label="再次输入密码 *" name="repwd" />
			<s:textfield label="M码*" name="mkey.mid" />
			<s:submit value="提 交" />
    	</s:form>
    </div>
  </body>
</html>
