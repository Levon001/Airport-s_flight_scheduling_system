package com.muxan.flightschedulingsystem.servlet;

import com.muxan.flightschedulingsystem.model.Airplane;
import com.muxan.flightschedulingsystem.repository.AirplaneRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reserveSeats")
public class ReserveSeatsServlet extends HttpServlet {

    private AirplaneRepo airplaneRepo = new AirplaneRepo();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int airplaneId = Integer.parseInt(request.getParameter("airplaneId"));
        Airplane airplane = airplaneRepo.getById(airplaneId);

        String jsonResponse = airplane.reserveSeats();
        if(jsonResponse.charAt(12) == 't') {
            airplaneRepo.updateByFreeSeats(airplaneId);
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

}

