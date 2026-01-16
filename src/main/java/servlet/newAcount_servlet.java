package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import model_Logic.newAcount_Logic;

@WebServlet("/newAcount_servlet")
public class newAcount_servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 新規登録画面に遷移
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

        String name = request.getParameter("name");
        String pass = request.getParameter("pass");

        // ===== 完全入力チェック =====
        if (isBlank(name) || isBlank(pass)) {

            request.setAttribute("errorMsg", "名前とパスワードは必須です");

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/WEB-INF/jsp/newAcount.jsp");
            dispatcher.forward(request, response);
            return;
        }
        // ==========================

        User user = new User(name.trim(), pass.trim());

        newAcount_Logic logic = new newAcount_Logic();
        boolean isRegistered = logic.execute(user);

        if (isRegistered) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
        }

        request.setAttribute("isRegistered", isRegistered);
        request.setAttribute("user", user);

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp_Result/newAcount_Result.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * null / 空文字 / 半角スペース / 全角スペースのみ を true
     */
    private boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        // 全角スペースを半角に変換してからtrim
        String replaced = str.replace("　", " ").trim();
        return replaced.isEmpty();
    }
}