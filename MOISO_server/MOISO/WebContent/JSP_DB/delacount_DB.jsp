<%@page import="DB_connection.Database"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("input_user_ID");
	User user = new User();
	user.setId(id);
	Database connect = new Database();
	connect.deleteAccount(user);	
%>
</body>
</html>