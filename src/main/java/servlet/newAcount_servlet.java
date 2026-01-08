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

/**
 * Servlet implementation class newAcount_servlet
 */
@WebServlet("/newAcount_servlet")
public class newAcount_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	//新規登録画面に遷移
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 RequestDispatcher dispatcher =
	                request.getRequestDispatcher("/WEB-INF/jsp/newAcount.jsp");
	        dispatcher.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	//登録作業のpostでDBに登録その結果、リザルトに遷移
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//受け取っ他データをDBに登録
		// ▼ フォーム値取得
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        
        System.out.println(request.getParameter("name") + " newAcount_servlet");
        System.out.println(request.getParameter("pass") + " newAcount_servlet");
        
     // ▼ User オブジェクト生成
        User user = new User(name, pass);
        
        newAcount_Logic newacountLogic = new newAcount_Logic();
        boolean isRegistered = newacountLogic.execute(user);
		
     // ▼ 登録成功ならセッションにユーザー情報を保存
        if (isRegistered) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            
            System.out.println(name + " newAcount_servlet.java");
            System.out.println(pass + " newAcount_servlet.java");
         }

        // ▼ リクエストスコープに結果保存（JSPに渡す用）
        request.setAttribute("isRegistered", isRegistered);
        request.setAttribute("user", user);
        
		RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp_Result/newAcount_Result.jsp");
        dispatcher.forward(request, response);
	}

}
