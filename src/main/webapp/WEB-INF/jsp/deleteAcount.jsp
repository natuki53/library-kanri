<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/search_form.css">
<title>アカウント削除</title>
</head>
<body>

<h1>アカウント削除</h1>

<p>ログアウトしてからアカウント削除をしてください</p>

<form action="deleteAcount_servlet" method="post">
    <label>ユーザー名</label><br>
    <input type="text" name="username" required><br>

    <label>パスワード</label><br>
    <input type="password" name="password" required><br><br>

    <button type="submit">アカウントを削除</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<div class="right-area">
    <a href="index.jsp">TOPへ</a>
</div>

</body>
</html>