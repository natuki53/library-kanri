<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<title>レンタル画面</title>
</head>
<body>
<%
String loginUser = (String) session.getAttribute("loginUser");
%>

<p>
ログイン状態：
<%= (loginUser != null) ? loginUser : "未ログイン" %>
</p>
<h1>レンタル画面</h1>

<!-- メッセージ表示 -->
<%
String message = (String) request.getAttribute("message");
if (message != null) {
%>
<p><%= message %></p>
<%
}
%>

<h2>書籍一覧</h2>
<a href="index.jsp">TOPへ</a>

<form action="rental_servlet" method="get">
    <button type="submit">一覧に戻る</button>
</form>

<!-- 検索フォーム（ここに追加） -->
<form action="rental_servlet" method="get">
    <input type="text" name="keyword" placeholder="書籍名で検索">
    <button type="submit">検索</button>
</form>
<table border="1">
<tr>
    <th>ID</th>
    <th>書籍名</th>
    <th>在庫数</th>
    <th>貸出</th>
    <th>返却</th>
</tr>

<%
List<Book> books = (List<Book>) request.getAttribute("books");
if (books != null) {
    for (Book book : books) {
%>
<tr>
    <td><%= book.getId() %></td>
    <td><%= book.getBook() %></td>
    <td><%= book.getNumber() %></td>
    <td>
    <% if (book.getNumber() > 0) { %>
    <form action="rental_servlet" method="post" style="margin:0;" onsubmit="disableButton(this)">
        <input type="hidden" name="bookId" value="<%= book.getId() %>">
        <input type="hidden" name="action" value="rent">
        <button type="submit">貸出</button>
    </form>
    <% } else { %>
    貸出不可
    <% } %>
</td>
<td>
    <form action="rental_servlet" method="post" style="margin:0;" onsubmit="disableButton(this)">
        <input type="hidden" name="bookId" value="<%= book.getId() %>">
        <input type="hidden" name="action" value="return">
        <button type="submit">返却</button>
    </form>
</td>
</tr>
<%
    }
}
%>

</table>

</body>
<script>
function disableButton(form) {
    const button = form.querySelector("button");
    button.disabled = true;
    button.textContent = "処理中...";
}
</script>
</html>