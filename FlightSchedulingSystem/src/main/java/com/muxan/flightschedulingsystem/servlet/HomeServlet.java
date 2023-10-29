package com.muxan.flightschedulingsystem.servlet;

import com.muxan.flightschedulingsystem.model.Flight;
import com.muxan.flightschedulingsystem.repository.FlightRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private FlightRepo flight = new FlightRepo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Flight> flightListIfDirectionOne = flight.getFlightsByDirection(1);
        req.setAttribute("flightsWithDirectionOne",flightListIfDirectionOne);
        req.getRequestDispatcher("WEB-INF/home.jsp").forward(req, resp);
    }
}
