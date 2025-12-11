<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理画面</title>
<link rel="stylesheet" href="CSS/admin.css">
</head>
<body>

	<h1>本登録</h1>

<!-- .javaに渡す -->
	<form action="Main" method="post">
		ID:<input type = "number" name = "id"><br>
		名前:<input type = "text" name = "name"><br>
		数量<input type = "number" name = "num">
		<input type = "submit" value = "登録">
		<a href="index.jsp">戻る</a>
	</form>
</body>
</html>