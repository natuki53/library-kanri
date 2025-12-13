<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Mutter" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>図書管理システム</title>
<link rel="stylesheet" href="CSS/search.css">
</head>
<body>
<!-- 左側 -->
<div class="left">
    <h1>検索</h1>

    <form action="search" method="post">
        <div class="form-group">
            <label>ID：</label><br>
            <input type="text" name="id">
        </div>

        <div class="form-group">
            <label>数量：</label><br>
            <input type="text" name="number">
        </div>

        <div class="form-group">
            <button type="submit">検索</button>
        </div>
    </form>

    <br>
    <form action="Logout" method="get">
        <button type="submit">ログアウト画面へ遷移</button>
    </form>
    <a href = "index.jsp">戻る</a>
</div>
<!-- 右側 -->
<div class="right">
    <h1>DB一覧</h1>

    <table>
        <tr>
            <th>数量</th>
            <th>書籍名</th>
        </tr>

        <%  
            List<Mutter> list = (List<Mutter>) request.getAttribute("list");
            if(list != null){
                for (Mutter m : list) {
        %>
                    <tr>
                        <td><%= m.getNumber() %></td>
                        <td><%= m.getBook() %></td>
                    </tr>
        <%      
                }
            }
        %>
    </table>
</div>

</body>
</html>
