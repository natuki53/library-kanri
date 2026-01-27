<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/search_form.css">
<title>ログアウト</title>
</head>
<body>

<h1><%= request.getAttribute("message") %></h1>

<a href="<%= request.getContextPath() %>/Login_servlet">ログイン画面へ</a>
<br>
<div class="right-area">
    <a href="index.jsp">TOPへ</a>
</div>
</body>
</html>