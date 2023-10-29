package com.muxan.flightschedulingsystem.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/scheduleList")
public class ScheduleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String direction = req.getParameter("directionDropdown");
        String country = req.getParameter("countriesDropdown");
        String airplane = req.getParameter("airplaneDropdown");

        String attributeDirectionCountryAirplaneID = direction + "," + country + "," + airplane;

        req.setAttribute("attributeDirectionCountryAirplaneID",attributeDirectionCountryAirplaneID);
        req.getRequestDispatcher("WEB-INF/freeGateRunwayPair.jsp").forward(req, resp);

    }
}
