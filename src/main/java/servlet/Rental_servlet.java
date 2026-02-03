package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.RentalLendDao;
import model.Book;
import model.Lend;
import model.User;

@WebServlet("/Rental_servlet")
public class Rental_servlet extends HttpServlet {

    private final RentalLendDao dao = new RentalLendDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ログイン状態取得（未ログイン可）
        HttpSession session = request.getSession(false);
        User loginUser =
            (session != null) ? (User) session.getAttribute("loginUser") : null;

        /* ===== 書籍一覧（常に取得） ===== */
        String keyword = request.getParameter("keyword");

        
        List<Book> books =
        	    (keyword == null || keyword.isEmpty())
        	        ? dao.getAllBooks()
        	        : dao.searchBooks(keyword);

        	/* ★ 追加：ログイン時のみ貸出状態を判定 */
        	if (loginUser != null) {
        	    for (Book book : books) {
        	        boolean lent =
        	            dao.isAlreadyLent(loginUser.getId(), book.getBook());
        	        book.setAlreadyLent(lent);
        	    }
        	}

        	request.setAttribute("books", books);
        
       System.out.println("Rental_servlet 43行目まで実行確認できた");
        /* ===== ログイン時のみ ===== */
        if (loginUser != null) {

            int remainLend = 3 - dao.countLend(loginUser.getId());
            request.setAttribute("remainLend", remainLend);

            List<Lend> lendList =
                dao.findLendingBooksByUser(loginUser.getId());
            request.setAttribute("lendList", lendList);
            System.out.println("Rental_servlet 行目まで実行確認できた");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/rental.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loginUser =
            (session != null) ? (User) session.getAttribute("loginUser") : null;

        // 貸出・返却はログイン必須
        if (loginUser == null) {
            request.setAttribute("popupMessage", "ログインしてください");
            doGet(request, response);
            return;
        }

        String action   = request.getParameter("action");
        String bookName = request.getParameter("bookname");

        int userId  = loginUser.getId();
        String name = loginUser.getName();

        if ("rent".equals(action)) {

            if (dao.isAlreadyLent(userId, bookName)) {
                request.setAttribute("popupMessage", "すでに借りています");

            } else if (dao.countLend(userId) >= 3) {
                request.setAttribute("popupMessage", "3冊以上は借りられません");

            } else if (dao.lendBook(userId, name, bookName)) {
                request.setAttribute("popupMessage", "貸出完了");

            } else {
                request.setAttribute("popupMessage", "在庫がありません");
            }

        } else if ("return".equals(action)) {

            boolean result = dao.returnBook(userId, bookName);
            request.setAttribute(
                "popupMessage",
                result ? "返却完了" : "返却できません"
            );
        }

        doGet(request, response);
    }
}