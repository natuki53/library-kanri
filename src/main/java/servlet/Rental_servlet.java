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

        RentalLendDao rentalDao = new RentalLendDao();
        ListDao listDao = new ListDao();

        List<Book> books = listDao.findAll();

        if (loginUser != null) {

            for (Book b : books) {
                b.setAlreadyLent(
                    rentalDao.isAlreadyLent(loginUser.getId(), b.getBook())
                );
            }

            int remain = 1 - rentalDao.countAllLend(loginUser.getId());
            request.setAttribute("remainLend", remain);

            request.setAttribute(
                "lendList",
                rentalDao.findLendingBooksByUser(loginUser.getId())
            );
        }

        // ★ PRG用メッセージ
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

        // ★ PRG（絶対必須）
        response.sendRedirect(request.getContextPath() + "/Rental_servlet");
    }
}