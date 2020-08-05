<%@page import="DB_connection.Database"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>로그인</title>
</head>
<body>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("input_user_ID");
	String pw = request.getParameter("input_user_PW");	
	
	Database account = new Database();
	if(account.login(id, pw)){
		out.print(id + "님 반갑습니다.");
	}
	else{
		out.print("로그인 실패");
	}  
%>
</body>
</html>