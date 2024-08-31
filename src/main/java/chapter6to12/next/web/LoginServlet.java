package chapter6to12.next.web;

import chapter6to12.core.db.DataBase;
import chapter6to12.next.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User findUser = DataBase.findUserById(userId);

        if(findUser!=null && password.equals(findUser.getPassword())){
            req.getSession().setAttribute("user", findUser);
        }

        resp.sendRedirect("/index.jsp");
    }
}
