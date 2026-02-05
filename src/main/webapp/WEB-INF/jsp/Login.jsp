<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/search_form.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/table.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/form.css">
<title>ログイン</title>
</head>
<body>

<!-- 
<a href = "index.jsp">
<div class="hover-img"></div>
</a>
-->

<a href ="index.jsp"><img src ="images/library.png"></a>
<div class="container">
	<h1>ログイン</h1>
    <!-- ログイン失敗などのエラーメッセージ -->
    <%
    String error = (String) request.getAttribute("error");
    if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <%
    }
    %>  
    <form action="Login_servlet" method="post">
        <p>
            ユーザー名<br>
            <input type="text" name="name">
        </p>
        <p>
            パスワード<br>
            <input type="password" name="pass">
        </p>
        <input type="submit" value="ログイン">
    </form>

    <form action="index.jsp" method="get">
        <input type="submit" value="トップに戻る">
    </form>

</div>

</body>
</html>