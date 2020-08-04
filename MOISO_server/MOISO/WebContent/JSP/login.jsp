<%@ page language="java" contentType="text/html;charset=UTF-8" 
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인이다</title>
</head>
<body>
	<form action="../JSP_DB/login_DB.jsp" method = "POST">
		<input name = "input_user_ID" type = "text"/>
		<input name = "input_user_PW" type = "text"/>
		<input name = "submit" type = "submit"/>
	</form>
</body>
</html>
