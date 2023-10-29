package com.muxan.flightschedulingsystem.listener;

import com.muxan.flightschedulingsystem.repository.RunwayRepo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class RunwayStatusUpdateListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

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
        RunwayRepo runwayRepo = new RunwayRepo();
        runwayRepo.deactivateRunwaysPastEndTime();
    }
}
