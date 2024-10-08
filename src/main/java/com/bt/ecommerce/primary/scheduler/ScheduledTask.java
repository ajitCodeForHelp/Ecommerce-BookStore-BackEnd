//package com.kpis.kpisFood.primary.scheduler;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class ScheduledTask {
//
//    @Value("${scheduler}")
//    private String scheduler;
//
//    // TODO > Implement Redis HERE
//    private static boolean Every24HourSchedulerState = false;
//
//    @Scheduled(cron = "0 0 0 * * *")
//    public void every24HourScheduler() {
//        log.info("every24HourScheduler()");
//        if (scheduler.equalsIgnoreCase("OFF")) {
//            return;
//        }
//        if (Every24HourSchedulerState) {
//            return;
//        }
//        Every24HourSchedulerState = true;
//        try {
//            // TODO > JOB
//
//        } finally {
//            Every24HourSchedulerState = false;
//        }
//    }
//
//}