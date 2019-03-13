package com.zbw911.tester.actuatortester.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {


    @Autowired
    private Actuatortester actuatortester;

    @Scheduled(fixedRate = 1000)
    public void testMeter() {
        actuatortester.testMeter();
    }
    @Scheduled(initialDelay=1000, fixedRate = 100000)
    public void testenvironmentEndpoint() {
        actuatortester.testenvironmentEndpoint();
    }

    @Scheduled(initialDelay=1000, fixedRate = 100000)
    public void testthreadDumpEndpoint() {
        actuatortester.testthreadDumpEndpoint();
    }
}