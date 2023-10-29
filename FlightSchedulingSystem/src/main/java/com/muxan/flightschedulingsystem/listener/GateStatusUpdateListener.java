package com.muxan.flightschedulingsystem.listener;

import com.muxan.flightschedulingsystem.repository.GateWithPeriodRepo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class GateStatusUpdateListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private final GateWithPeriodRepo gate = new GateWithPeriodRepo();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateFlightStatusTask, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private void updateFlightStatusTask() {
        gate.deactivateGatesIsActive();
    }
}
