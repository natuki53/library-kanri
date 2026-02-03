<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>
<%@ page import="model.Lend" %>
<%@ page import="model.User" %>

<%
/*
 * ログイン状態は session から取得（確定仕様）
 */
User loginUser = (User) session.getAttribute("loginUser");

/*
 * 以下は Rental_servlet から request にセットされる想定
 * 未ログイン時は null で問題ない
 */
Integer remainLend = (Integer) request.getAttribute("remainLend");
String popupMessage = (String) request.getAttribute("popupMessage");
List<Book> books = (List<Book>) request.getAttribute("books");
List<Lend> lendList = (List<Lend>) request.getAttribute("lendList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>レンタル画面</title>

<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/search_form.css">

<style>
    table {
        border-collapse: collapse;
        width: 80%;
        margin-top: 20px;
    }
    th, td {
        padding: 8px;
        text-align: center;
        border: 1px solid #ccc;
    }
    .right-area {
        text-align: right;
        margin-bottom: 10px;
    }
</style>
</head>

<body>

<h1>レンタル画面</h1>

<div class="right-area">
    <a href="index.jsp">TOPへ</a>
</div>

<% if (loginUser != null) { %>
    <p>ログイン中：<strong><%= loginUser.getName() %></strong></p>
    <p>あと <strong><%= remainLend %></strong> 冊借りられます</p>
<% } else { %>
    <p style="color:red;">※ 書籍の貸出にはログインが必要です</p>
<% } %>

<!-- 検索フォーム -->
<form action="Rental_servlet" method="get" class="search-form" style="display:inline;">
    <input type="text" name="keyword" placeholder="書籍名で検索">
    <button type="submit">検索</button>
</form>

<form action="Rental_servlet" method="get" class="search-form" style="display:inline;">
    <button type="submit">一覧に戻る</button>
</form>

<% if (loginUser != null) { %>
<form action="MyLibrary_servlet" method="get" class="search-form" style="display:inline;">
    <button type="submit">Myライブラリ</button>
</form>
<% } %>

<!-- 書籍一覧 -->
<table>
    <tr>
        <th>書籍名</th>
        <th>在庫数</th>
        
        <% if (loginUser != null) { %>
        <th>貸出</th>
        <% } %>
        
    </tr>

<% if (books != null && !books.isEmpty()) {
       for (Book book : books) { %>
<tr>
    <td><%= book.getBook() %></td>
    <td><%= book.getNumber() %></td>
    
     <% if (loginUser != null) { %>
    <td>
        <form action="Rental_servlet" method="post">
            <input type="hidden" name="action" value="rent">
            <input type="hidden" name="bookname" value="<%= book.getBook() %>">
            <button type="submit"
    			class="<%= book.isAlreadyLent() ? "lent" : "available" %>"
   				 <%= book.isAlreadyLent()
       			 || (remainLend != null && remainLend <= 0)
       			 || book.getNumber() <= 0
        		? "disabled" : "" %>>
   				 <%= book.isAlreadyLent() ? "貸出中" : "貸出" %>
			</button>
        </form>
    </td>
    <% } %>
</tr>
<%     }
   } else { %>
<tr>
    <td colspan="3">書籍がありません</td>
</tr>
<% } %>
</table>

<!-- ポップアップ表示 -->
<% if (popupMessage != null) { %>
<script>
    alert("<%= popupMessage.replace("\"", "\\\"") %>");
</script>
<% } %>

</body>
</html>