package com.muxan.flightschedulingsystem.servlet;

import com.muxan.flightschedulingsystem.model.User;
import com.muxan.flightschedulingsystem.repository.UserRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final String ADMIN_USERNAME = "ADMIN";
    private static final String ADMIN_PASSWORD = "picsart";

    private UserRepo userRepo = new UserRepo();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (ADMIN_USERNAME.equals(email) && ADMIN_PASSWORD.equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("userType", "admin");
            resp.sendRedirect("/adminPage");  // Redirect to the admin page
            return;
        }

        User user = userRepo.getByEmailAndPassword(email, password);
        HttpSession session = req.getSession();
        if(user != null) {
            session.setAttribute("user",user);
            session.setAttribute("userType", "user");
            resp.sendRedirect("/home");
        } else {
            session.setAttribute("msg","Username or password is incorrect");
            resp.sendRedirect("/");
        }
    }
}
