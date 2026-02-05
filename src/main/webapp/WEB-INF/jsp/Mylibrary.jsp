<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Lend" %>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/table.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/form.css">
<title>Myライブラリ</title>
</head>
<body>

<!-- 
<a href = "index.jsp">
<div class="hover-img"></div>
</a>
-->

<a href ="index.jsp"><img src ="images/library.png"></a>
<div class="container">
	<h1>Myライブラリ</h1>

    <%
    User loginUser = null;
    if (session != null) {
        Object obj = session.getAttribute("loginUser");
        if (obj instanceof User) {
            loginUser = (User) obj;
        }
    }

    // Servletから渡された貸出リスト
    List<Lend> lendList = (List<Lend>) request.getAttribute("lendList");
    %>

    <% if (loginUser != null) { %>
        <p class="login ok">
            ログイン中：<%= loginUser.getName() %> さん
        </p>

        <h2>現在借りている本</h2>

        <% if (lendList != null && !lendList.isEmpty()) { %>
        <table class="rental-table">
            <tr>
                <th>書籍名</th>
                <th>貸出日</th>
                <th>返却期限</th>
            </tr>
            <% for (Lend lend : lendList) { %>
            <tr>
                <td><%= lend.getBookname() %></td>
                <td><%= lend.getLendDate() %></td>
                <td>
                    <%= new java.sql.Date(
                        lend.getLendDate().getTime()
                        + 14L * 24 * 60 * 60 * 1000) %>
                </td>
            </tr>
            <% } %>
        </table>
        <% } else { %>
            <p>現在借りている本はありません。</p>
        <% } %>

        <p>※レンタルは上限3冊までです。</p>

    <% } else { %>
        <p class="login ng">ログインが必要です。</p>
    <% } %>

    <hr>

    <form action="index.jsp" method="get">
        <input type="submit" value="トップに戻る">
    </form>

    <form action="Rental_servlet" method="get">
        <input type="submit" value="貸出・検索">
    </form>

</div>

</body>
</html>