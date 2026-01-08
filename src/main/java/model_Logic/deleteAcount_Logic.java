package model_Logic;

import dao.UserDao;

public class deleteAcount_Logic {

    public boolean execute(int userId) {
        UserDao dao = new UserDao();
        return dao.deleteUser(userId);
    }
}