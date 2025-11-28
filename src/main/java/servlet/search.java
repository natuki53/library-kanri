package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 図書検索メニューへ遷移するサーブレット
 * index.jsp → /search → main.jsp
 */
@WebServlet("/search")
public class search extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 初期処理（特別な初期化が不要なため空のまま）
     */
    public search() {
        super();
    }

    /**
     * GETリクエスト時（直接アクセスされた場合）
     * システムとしては POST 遷移を前提とするため、メニュー(main.jsp)へフォワードする。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("jsp_Result/searchResult.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * index.jsp の検索ボタンクリック（POST）で呼ばれるメソッド
     * ここでは検索画面（ WEB-INF/search.jsp）に遷移するだけ。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("WEB-INF/jsp/search.jsp");
        dispatcher.forward(request, response);
    }
}