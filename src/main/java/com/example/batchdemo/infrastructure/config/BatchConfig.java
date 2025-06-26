package com.example.batchdemo.infrastructure.config;

import com.example.batchdemo.domain.model.LoanRecord;
import com.example.batchdemo.infrastructure.batch.processor.LoanRecordProcessor;
import com.example.batchdemo.infrastructure.batch.reader.MultiFileCsvReader;
import com.example.batchdemo.infrastructure.sftp.SftpService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchConfig {

    @Autowired
    private SftpService sftpService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private LoanRecordProcessor loanRecordProcessor;

    @Bean
    public Job sftpCsvToDbJob(JobRepository jobRepository, Step sftpCsvToDbStep) {
        return new JobBuilder("sftpCsvToDbJob", jobRepository)
                .start(sftpCsvToDbStep)
                .build();
    }

    @Bean
    public Step sftpCsvToDbStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sftpCsvToDbStep", jobRepository)
                .<String[], LoanRecord>chunk(10, transactionManager)
                .reader(csvReader())
                .processor(loanRecordProcessor)
                .writer(loanRecordWriter())
                .build();
    }

    @Value("${sftp.input.directory}")
    private String sftpInputDirectory;

    @Bean
    public ItemReader<String[]> csvReader() {
        return new MultiFileCsvReader(sftpService, sftpInputDirectory);
    }

    @Bean
    public ItemWriter<LoanRecord> loanRecordWriter() {
        return new JpaItemWriterBuilder<LoanRecord>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
