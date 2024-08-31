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

@WebServlet("/user/list")
public class ListUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/index.jsp");
        } else {
            req.setAttribute("users", DataBase.findAll());
            RequestDispatcher rd = req.getRequestDispatcher("/user/list.jsp");
            rd.forward(req, resp);
        }

    }
}
