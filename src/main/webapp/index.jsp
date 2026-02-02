<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Lend" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<title>図書管理システム</title>
</head>
<body>

<h1>図書管理システム</h1>

<!-- ログイン状態表示 -->
<%
    User loginUser = null;

    if (session != null) {
        Object obj = session.getAttribute("loginUser");
        if (obj instanceof User) {
            loginUser = (User) obj;
        }
    }
%>

<% if (loginUser != null) { %>
    <p style="color: green; font-weight: bold;">
        ログイン中：<%= loginUser.getName() %> さん
    </p>
<% } else { %>
    <p style="color: red; font-weight: bold;">
        ログインしていません
    </p>
<% } %>

<table>
<tr>
    <th>
	<% if (loginUser == null) { %>
    	<form action="newAcount_servlet" method="get">
        	<input type="submit" value="新規登録">
    	</form>
	<% }else{ %>
		<p>新規登録できないよ！</p>
	<% } %>
    </th>
    <th>
        <form action="Login_servlet" method="get">
            <input type="submit" value="ログイン">
        </form>
    </th>
</tr>
<tr>
    <th>
        <form action="Rental_servlet" method="get">
            <input type="submit" value="本　検索">
        </form>
    </th>
    <th>
        <form action="Logout_servlet" method="get">
            <input type="submit" value="ログアウト">
        </form>
    </th>
</tr>
</table>
<%@ page import="java.util.List" %>
<%@ page import="model.Lend" %>

<%
List<Lend> lendList = (List<Lend>) request.getAttribute("lendList");
%>

<% if (loginUser != null) { %>
    <h2>現在借りている本</h2>

    <% if (lendList != null && !lendList.isEmpty()) { %>
        <table border="1">
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
                            + 7L * 24 * 60 * 60 * 1000) %>
                    </td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <p>現在借りている本はありません。</p>
    <% } %>
<% } %>
</body>
</html>