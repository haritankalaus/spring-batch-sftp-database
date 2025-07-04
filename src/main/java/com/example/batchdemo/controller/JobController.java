package com.example.batchdemo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job sftpCsvToDbJob;

    @PostMapping("/sftp-import")
    public ResponseEntity<String> triggerSftpImport() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            jobLauncher.run(sftpCsvToDbJob, jobParameters);
            return ResponseEntity.ok("Job triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error triggering job: " + e.getMessage());
        }
    }
}
