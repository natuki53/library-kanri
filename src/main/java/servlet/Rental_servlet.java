package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.LendDao;
import dao.RentalDao;
import model.Book;
import model.User;

@WebServlet("/Rental_servlet")
public class Rental_servlet extends HttpServlet {

    private RentalDao rentalDao = new RentalDao();
    private LendDao lendDao = new LendDao();

    /**
     * 一覧表示・検索
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // session から User を取得
        User loginUser = (session != null)
                ? (User) session.getAttribute("loginUser")
                : null;

        request.setAttribute("loginUser", loginUser);

        // ログイン時のみ検索可能
        if (loginUser != null) {

            String keyword = request.getParameter("keyword");

            List<Book> books = (keyword == null || keyword.isEmpty())
                    ? rentalDao.getAllBooks()
                    : rentalDao.searchBooks(keyword);

            request.setAttribute("books", books);

            // 貸出残数計算（名前が必要なときだけ getName）
            int remain = 3 - lendDao.countLend(loginUser.getName());
            request.setAttribute("remainLend", remain);
        }

        RequestDispatcher rd =
                request.getRequestDispatcher("/WEB-INF/jsp/rental.jsp");
        rd.forward(request, response);
    }

    /**
     * 貸出・返却
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loginUser = (session != null)
                ? (User) session.getAttribute("loginUser")
                : null;

        // 未ログイン対策
        if (loginUser == null) {
            request.setAttribute("popupMessage", "ログインしてください");
            doGet(request, response);
            return;
        }

        String action = request.getParameter("action");
        String bookname = request.getParameter("bookname");
        String userName = loginUser.getName();

        if ("rent".equals(action)) {

            if (lendDao.isAlreadyLent(userName, bookname)) {
                request.setAttribute("popupMessage", "すでに借りています");
            } else if (lendDao.countLend(userName) >= 3) {
                request.setAttribute("popupMessage", "3冊以上は借りられません");
            } else {
                lendDao.lendBook(userName, bookname);
                request.setAttribute("popupMessage", "貸出完了");
            }

        } else if ("return".equals(action)) {

            if (!lendDao.isAlreadyLent(userName, bookname)) {
                request.setAttribute("popupMessage", "この本は借りていません");
            } else {
                boolean result = lendDao.returnBook(userName, bookname);

                request.setAttribute(
                        "popupMessage",
                        result ? "返却完了" : "返却処理に失敗しました"
                );
            }
        }

        doGet(request, response);
    }
}