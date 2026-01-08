<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント削除</title>
</head>
<body>

<h2>アカウント削除</h2>

<p>本当にアカウントを削除しますか？</p>

<form action="deleteAcount_servlet" method="post">
    <input type="submit" value="削除する">
</form>

<a href="menu.jsp">戻る</a>

</body>
</html>