package com.muxan.flightschedulingsystem.servlet;

import com.muxan.flightschedulingsystem.model.Airplane;
import com.muxan.flightschedulingsystem.model.Flight;
import com.muxan.flightschedulingsystem.model.GateWithPeriod;
import com.muxan.flightschedulingsystem.model.RunwayWithPeriod;
import com.muxan.flightschedulingsystem.repository.AirplaneRepo;
import com.muxan.flightschedulingsystem.repository.FlightRepo;
import com.muxan.flightschedulingsystem.repository.GateWithPeriodRepo;
import com.muxan.flightschedulingsystem.repository.RunwayRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/createFlight")
public class CreateFlightServlet extends HttpServlet {

    private final FlightRepo flightRepo = new FlightRepo();
    private final AirplaneRepo airplaneRepo = new AirplaneRepo();
    private final RunwayRepo runwayRepo = new RunwayRepo();
    private final GateWithPeriodRepo gateRepo = new GateWithPeriodRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String allAttribute = req.getParameter("allAttribute");

        String[] attributes = allAttribute.split(",");

        int direction = attributes[0] != null && attributes[0].equals("to") ? 1 : 0;
        String country = attributes[1];
        int airplaneId = Integer.parseInt(attributes[2]);


        String selectedPair = req.getParameter("check");
        if (selectedPair == null || selectedPair.trim().isEmpty()) {
            req.setAttribute("attributeDirectionCountryAirplaneID", allAttribute); // Set the attribute here
            req.getRequestDispatcher("WEB-INF/freeGateRunwayPair.jsp").forward(req, resp);
            return;
        }

        String[] ids = selectedPair.split(",");

        int gateId = Integer.parseInt(ids[1]);
        int runwayId = Integer.parseInt(ids[0]);


        if(direction == 1) {
            gateId = Integer.parseInt(ids[0]);
            runwayId = Integer.parseInt(ids[1]);
        }


        runwayRepo.setActive(runwayId);
        gateRepo.setIsActive(gateId);


        Airplane airplane = airplaneRepo.getById(airplaneId);
        RunwayWithPeriod runway = runwayRepo.getById(runwayId);
        GateWithPeriod gate = gateRepo.getById(gateId);

        Flight flight = new Flight(direction,country,airplane,runway,gate);
        flightRepo.save(flight);

        req.getRequestDispatcher("WEB-INF/admin.jsp").forward(req, resp);
    }
}
