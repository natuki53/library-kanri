package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.LendDao;
import model_Logic.deleteAcount_Logic;

@WebServlet("/deleteAcount_servlet")
public class deleteAcount_servlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }

	    request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount.jsp")
	           .forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("UTF-8");

	    String username = request.getParameter("username");
	    String password = request.getParameter("password");

	    // 入力チェック
	    if (username == null || password == null ||
	        username.isEmpty() || password.isEmpty()) {

	        request.setAttribute("error", "未入力の項目があります");
	        request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount.jsp")
	               .forward(request, response);
	        return;
	    }

	    // 未返却チェック
	    LendDao lendDao = new LendDao();
	    if (lendDao.hasLendingBooks(username)) {
	        request.setAttribute("error", "本を返却してからアカウント削除してください");
	        request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount.jsp")
	               .forward(request, response);
	        return;
	    }

	    // アカウント削除
	    deleteAcount_Logic logic = new deleteAcount_Logic();
	    boolean result = logic.execute(username, password);

	    if (result) {
	        request.getRequestDispatcher("/WEB-INF/jsp_Result/deleteAcount_Result.jsp")
	               .forward(request, response);
	    } else {
	        request.setAttribute("error", "ユーザー名またはパスワードが正しくありません");
	        request.getRequestDispatcher("/WEB-INF/jsp/deleteAcount.jsp")
	               .forward(request, response);
	    }
	}
}