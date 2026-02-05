<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/table.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/form.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/search_form.css">
<title>新規登録</title>
</head>
<body>

<!-- 
<a href = "index.jsp">
<div class="hover-img"></div>
</a>
-->

<a href ="index.jsp"><img src ="images/library.png"></a>
<div class="container">

    <h1>新規登録</h1>

    <!-- エラーメッセージ表示 -->
    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null) {
    %>
        <p style="color:red;"><%= errorMsg %></p>
    <%
        }
    %>

    <form action="newAcount_servlet" method="post">
        <p>
            ユーザー名<br>
            <input type="text" name="name" value="<%= request.getParameter("name") != null ? request.getParameter("name") : "" %>">
        </p>

        <p>
            パスワード<br>
            <input type="password" name="pass">
        </p>

        <input type="submit" value="登録">
    </form>

    <form action="index.jsp" method="get">
        <input type="submit" value="トップに戻る">
    </form>

</div>

</body>
</html>