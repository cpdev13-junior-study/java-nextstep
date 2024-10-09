package chapter6to12.next.service;

import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.Service;
import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;

@Service
public class UserService {
    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void update(User user){
        User findUser = userDao.findByUserId(user.getUserId());
        findUser.setEmail(user.getEmail());
        findUser.setPassword(user.getPassword());
        findUser.setName(user.getName());
        userDao.update(findUser);
    }
}
