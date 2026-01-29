<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>
<%@ page import="model.User" %>

<%
User loginUser = (User) request.getAttribute("loginUser");
Integer remainLend = (Integer) request.getAttribute("remainLend");
String popupMessage = (String) request.getAttribute("popupMessage");
List<Book> books = (List<Book>) request.getAttribute("books");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/search_form.css">
<title>レンタル画面</title>
</head>
<body>

<h1>レンタル画面</h1>

<div class="right-area">
    <a href="index.jsp">TOPへ</a>
</div>

<% if (loginUser != null) { %>

    <p>ログイン中：<strong><%= loginUser.getName() %></strong></p>
    <p>あと <strong><%= remainLend %></strong> 冊借りられます</p>

    <!-- 検索フォーム -->
    <form action="Rental_servlet" method="get" class="search-form" style="display:inline;">
        <input type="text" name="keyword" placeholder="書籍名で検索">
        <button type="submit">検索</button>
    </form>

    <!-- 更新ボタン（検索条件リセット） -->
    <form action="Rental_servlet" method="get" class="search-form" style="display:inline;">
        <button type="submit">更新</button>
    </form>

    <table border="1">
        <tr>
            <th>書籍名</th>
            <th>在庫数</th>
            <th>貸出</th>
            <th>返却</th>
        </tr>

        <% if (books != null) { %>
            <% for (Book book : books) { %>
            <tr>
                <td><%= book.getBook() %></td>
                <td><%= book.getNumber() %></td>

                <td>
                    <form action="Rental_servlet" method="post" class="search-form">
                        <input type="hidden" name="action" value="rent">
                        <input type="hidden" name="bookname" value="<%= book.getBook() %>">
                        <button type="submit">貸出</button>
                    </form>
                </td>

                <td>
                    <form action="Rental_servlet" method="post" class="search-form">
                        <input type="hidden" name="action" value="return">
                        <input type="hidden" name="bookname" value="<%= book.getBook() %>">
                        <button type="submit">返却</button>
                    </form>
                </td>
            </tr>
            <% } %>
        <% } %>
    </table>

<% } else { %>

    <h2>ログインしないと利用できません</h2>

<% } %>

<!-- ポップアップ表示（独立） -->
<% if (popupMessage != null) { %>
<script>
    alert("<%= popupMessage %>");
</script>
<% } %>

</body>
</html>