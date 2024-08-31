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

@WebServlet("/user/update")
public class UpdateUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        req.setAttribute("user", user);
        RequestDispatcher rd = req.getRequestDispatcher("/user/update.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        User loginUser = (User) req.getSession().getAttribute("user");
        if (!loginUser.getUserId().equals(userId)) {
            throw new RuntimeException("수정할 수 없습니다.");
        }
        User findUser = DataBase.findUserById(userId);
        findUser.setEmail(email);
        findUser.setPassword(password);
        findUser.setName(name);
        resp.sendRedirect("/user/list");
    }
}
