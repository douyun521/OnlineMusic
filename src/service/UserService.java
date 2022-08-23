package service;

import dao.UserDao;
import entity.User;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: douYun
 * Date: 2021-02-27
 * Time: 12:34
 */
public class UserService {
    public User login(User loginUser) {
        UserDao userDao = new UserDao();
        User user = userDao.login(loginUser);
        return user;
    }
}