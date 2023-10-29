package com.muxan.flightschedulingsystem.servlet;

import com.muxan.flightschedulingsystem.model.User;
import com.muxan.flightschedulingsystem.repository.UserRepo;
import com.muxan.flightschedulingsystem.util.EmailUtil;
import com.muxan.flightschedulingsystem.util.NameSurnameUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserRepo userRepo = new UserRepo();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String msg = "";
        if (name == null || name.trim().equals("")) {
            msg += "Name is required<br>";
        } else if (NameSurnameUtil.containsInvalidCharacters(name)) {
            msg += "Name contains invalid characters<br>";
        }

        if (surname == null || surname.trim().equals("")) {
            msg += "Surname is required<br>";
        } else if (NameSurnameUtil.containsInvalidCharacters(surname)) {
            msg += "Surname contains invalid characters<br>";
        }
        if (email == null || email.trim().equals("")) {
            msg += "Email is required<br>";
        } else if (!EmailUtil.patternMatches(email)) {
            msg += "Email format is incorrect<br>";
        }
        if (password == null || password.trim().equals("")) {
            msg += "Password is required <br>";
        } else if (password.length() < 6) {
            msg += "Password length must be >= 6 <br>";
        }
        if (msg.equals("")) {
            User user = userRepo.getByEmail(email);
            if (user == null) {
                userRepo.save(User.builder()
                        .name(name)
                        .surname(surname)
                        .email(email)
                        .password(password)
                        .build());
            }
            resp.sendRedirect("/");
        }else {
            req.setAttribute("msg", msg);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }

    }
}
