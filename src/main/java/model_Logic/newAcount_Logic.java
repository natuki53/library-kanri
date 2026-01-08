package model_Logic;

import dao.UserDao;
import model.User;

public class newAcount_Logic {

	public boolean execute(User user) {
		UserDao dao = new UserDao();
		System.out.println("newAcountDao");
        return dao.create(user);   // ← ここで DB に登録
	}

}
