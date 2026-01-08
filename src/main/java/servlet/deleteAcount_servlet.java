package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model_Logic.deleteAcount_Logic;

@WebServlet("/deleteAcount_servlet")
public class deleteAcount_servlet extends HttpServlet {

    // 確認画面表示（GET）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 未ログインの場合 → 理由付きでログイン画面へ
        if (session == null || session.getAttribute("userId") == null) {
            request.setAttribute("loginReason", "deleteAccount");
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/WEB-INF/jsp/Login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // ログイン済み → 削除確認画面へ
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount.jsp");
        dispatcher.forward(request, response);
    }

    // 削除処理（POST）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // セッション切れ対策
        if (session == null || session.getAttribute("userId") == null) {
            request.setAttribute("loginReason", "deleteAccount");
            request.getRequestDispatcher("/WEB-INF/jsp/Login.jsp")
                   .forward(request, response);
            return;
        }

        int userId = (int) session.getAttribute("userId");

        deleteAcount_Logic logic = new deleteAcount_Logic();
        boolean result = logic.execute(userId);

        if (result) {
            session.invalidate(); // セッション破棄
            request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount_Result.jsp")
                   .forward(request, response);
        } else {
            request.setAttribute("error", "アカウント削除に失敗しました");
            request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount.jsp")
                   .forward(request, response);
        }
    }
}