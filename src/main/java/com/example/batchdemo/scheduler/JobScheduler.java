package com.example.batchdemo.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job sftpCsvToDbJob;

    // Run at 6:00 AM
    @Scheduled(cron = "${job.cron.morning}")
    public void runMorningJob() {
        runJob("morning");
    }

    // Run at 2:00 PM
    @Scheduled(cron = "${job.cron.afternoon}")
    public void runAfternoonJob() {
        runJob("afternoon");
    }

    // Run at 10:00 PM
    @Scheduled(cron = "${job.cron.evening}")
    public void runEveningJob() {
        runJob("evening");
    }

    private void runJob(String timeOfDay) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("timeOfDay", timeOfDay)
                    .toJobParameters();
            
            logger.info("Starting scheduled job for {}", timeOfDay);
            jobLauncher.run(sftpCsvToDbJob, jobParameters);
            logger.info("Completed scheduled job for {}", timeOfDay);
        } catch (Exception e) {
            logger.error("Error running scheduled job for {}: {}", timeOfDay, e.getMessage());
        }
    }
}
