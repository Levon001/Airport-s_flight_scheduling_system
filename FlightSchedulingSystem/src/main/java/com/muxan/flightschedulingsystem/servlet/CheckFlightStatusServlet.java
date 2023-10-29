package com.muxan.flightschedulingsystem.servlet;

import com.muxan.flightschedulingsystem.repository.FlightRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/checkFlightStatus")
public class CheckFlightStatusServlet extends HttpServlet {
    private FlightRepo flightRepo = new FlightRepo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int flightId = Integer.parseInt(req.getParameter("flightId"));
        resp.setContentType("application/json");
        resp.getWriter().write("{\"isActive\": " + flightRepo.getIsActive(flightId) + "}");
    }
}