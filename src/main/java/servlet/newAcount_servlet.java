package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;

@WebServlet("/newAcount_servlet")
public class newAcount_servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DB 接続情報
    private static final String URL =
        "jdbc:mysql://localhost:3306/library-touroku";
    private static final String USER = "root";
    private static final String PASS = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher =
            request.getRequestDispatcher("/WEB-INF/jsp/newAcount.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ===== パラメータ取得 =====
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");

        // ===== 正規化（最重要）=====
        name = normalize(name);
        pass = normalize(pass);

        // ===== 入力チェック =====
        if (isBlank(name) || isBlank(pass)) {
            request.setAttribute("errorMsg", "名前とパスワードは必須です");
            RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/newAcount.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // ===== DB登録 =====
        User user = null;
        boolean isRegistered = false;

        String sql = "INSERT INTO user(name, pass) VALUES(?, ?)";
         
        System.out.println("newAcount_servletの69行目まで実行できた");
        
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps =
                 con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, pass);

            System.out.println("78行目まで実行できた");
            
            int count = ps.executeUpdate();
            if (count > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    user = new User(id, name, pass);
                    isRegistered = true;

                    HttpSession session = request.getSession();
                    session.setAttribute("loginUser", user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute(
                "errorMsg",
                "登録に失敗しました（ユーザー名が既に存在する可能性があります）"
            );
        }

        request.setAttribute("isRegistered", isRegistered);
        request.setAttribute("user", user);

        RequestDispatcher dispatcher =
            request.getRequestDispatcher("/WEB-INF/jsp_Result/newAcount_Result.jsp");
        dispatcher.forward(request, response);
    }

    // ===== 正規化 =====
    // 前後の半角・全角空白を除去
    private String normalize(String str) {
        if (str == null) return null;
        return str.replaceAll("^[\\s　]+|[\\s　]+$", "");
    }

    // ===== 空判定 =====
    // null / 空文字 / 半角空白 / 全角空白のみ true
    private boolean isBlank(String str) {
        if (str == null) return true;
        String replaced = str.replace("　", " ").trim();
        return replaced.isEmpty();
    }
}