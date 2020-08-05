
<%@page import="DB_connection.Database"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입이다</title>
</head>
<body>
<%	
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("input_user_ID");
	String pw = request.getParameter("input_user_PW");
	String pwc = request.getParameter("input_user_PWC");
	String email = request.getParameter("input_user_Email");
	String nickname = request.getParameter("input_user_Nickname");
	
	User newAccount = new User(id,pw,pwc,email,nickname);
	newAccount.showUser();
	
	Database ismember = new Database();
	
	if(!ismember.isMember(newAccount)){	//해당 아이디의 존재 유무 확인
		out.print("로그인 가능");
		if(newAccount.getPassword().equals(newAccount.getPassword_confirm())){	//비밀번호 확인
			new Database().signup(newAccount);
			out.print("회원가입 성공");
		}
		else{
			out.print("비밀번호를 다시 확인해주세요");
		}
	}
	else{
		out.print("해당 아이디가 존재 합니다.");
	}
	
	
%>
</body>
</html>