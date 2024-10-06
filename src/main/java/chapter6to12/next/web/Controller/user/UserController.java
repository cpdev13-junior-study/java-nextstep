package chapter6to12.next.web.controller.user;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.annotation.RequestMethod;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.ModelAndView;
import chapter6to12.next.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController extends AbstractNewController {

    private final UserDao userDao;
    private final UserService userService;

    @Inject
    public UserController(UserDao userDao,UserService userService) {
        this.userDao = userDao;
        this.userService = userService;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("user : {}", user);

        userDao.insert(user);
        return jspView("redirect:/");
    }

    @RequestMapping("/user/create")
    public ModelAndView createUserForm(HttpServletRequest req, HttpServletResponse resp) {
        return jspView("/user/form.jsp");
    }

    @RequestMapping(value = "/user/list")
    public ModelAndView userList(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            return jspView("redirect:/");
        } else {
            return jspView("/user/list.jsp")
                    .addObject("users", userDao.findAll());
        }
    }

    @RequestMapping(value = "/user/update")
    public ModelAndView updateUserForm(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId);
        return jspView("/user/update.jsp")
                .addObject("user", user);
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        User updateUser = new User(userId, password, name, email);
        User loginUser = (User) req.getSession().getAttribute("user");

        if (!loginUser.getUserId().equals(userId)) {
            throw new RuntimeException("수정할 수 없습니다.");
        }
        userService.update(updateUser);
        return jspView("redirect:/user/list");
    }
}