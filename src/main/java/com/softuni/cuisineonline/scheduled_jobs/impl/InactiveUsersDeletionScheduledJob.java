package com.softuni.cuisineonline.scheduled_jobs.impl;

import com.softuni.cuisineonline.scheduled_jobs.ScheduledJob;
import com.softuni.cuisineonline.service.services.domain.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InactiveUsersDeletionScheduledJob implements ScheduledJob {

    private static final int USER_DELETION_FOR_INACTIVITY_PERIOD_IN_MONTHS = 6;

    private final UserService userService;

    public InactiveUsersDeletionScheduledJob(UserService userService) {
        this.userService = userService;
    }

    /**
     * Job is scheduled to execute every day in 3:00:00 am
     */
    @Override
    @Scheduled(cron = "0 0 3 * * *")
    public void execute() {
        userService.deleteInactiveUsers(USER_DELETION_FOR_INACTIVITY_PERIOD_IN_MONTHS);
    }

}
