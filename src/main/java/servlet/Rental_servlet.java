package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ListDao;
import dao.RentalLendDao;
import model.Book;
import model.User;

@WebServlet("/Rental_servlet")
public class Rental_servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        ListDao listDao = new ListDao();
        RentalLendDao rentalDao = new RentalLendDao();

        // 本一覧（在庫は list.number をそのまま使う）
        List<Book> books = listDao.findAll();

        if (loginUser != null) {

            // 既に借りている本の判定（14日以内）
            for (Book b : books) {
                b.setAlreadyLent(
                    rentalDao.isAlreadyLent(loginUser.getId(), b.getBook())
                );
            }

            // ユーザー残り貸出可能数（上限 − 有効貸出数）
            int remainLend =
                    RentalLendDao.MAX_LEND
                  - rentalDao.countValidLend(loginUser.getId());

            request.setAttribute("remainLend", remainLend);

            // 貸出中一覧（14日以内）
            request.setAttribute(
                "lendList",
                rentalDao.findLendingBooksByUser(loginUser.getId())
            );
        }

        // PRGメッセージ
        String msg = (String) session.getAttribute("popupMessage");
        if (msg != null) {
            request.setAttribute("popupMessage", msg);
            session.removeAttribute("popupMessage");
        }

        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/jsp/rental.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser != null &&
            "rent".equals(request.getParameter("action"))) {

            RentalLendDao dao = new RentalLendDao();
            boolean result = dao.lendBook(
                loginUser.getId(),
                loginUser.getName(),
                request.getParameter("bookname")
            );

            session.setAttribute(
                "popupMessage",
                result ? "貸出が完了しました" : "これ以上借りられません"
            );
        }

        // PRG
        response.sendRedirect(request.getContextPath() + "/Rental_servlet");
    }
}