package com.example.batchdemo.job;

import com.example.batchdemo.model.Customer;
import com.example.batchdemo.processor.CustomerProcessor;
import com.example.batchdemo.service.SftpService;
import com.example.batchdemo.reader.MultiFileReader;
import com.example.batchdemo.reader.FileReaderFactory;
import com.example.batchdemo.listener.SftpSessionJobListener;

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
public class SftpToDatabaseJob {

    @Autowired
    private SftpService sftpService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CustomerProcessor customerProcessor;

    @Autowired
    private SftpSessionJobListener sftpSessionJobListener;

    @Bean
    public Job sftpCsvToDbJob(JobRepository jobRepository, Step sftpCsvToDbStep) {
        return new JobBuilder("sftpCsvToDbJob", jobRepository)
                .start(sftpCsvToDbStep)
                .listener(sftpSessionJobListener)
                .build();
    }

    @Bean
    public Step sftpCsvToDbStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sftpCsvToDbStep", jobRepository)
                .<String[], Customer>chunk(10, transactionManager)
                .reader(fileReader())
                .processor(customerProcessor)
                .writer(customerWriter())
                .build();
    }

    @Value("${sftp.input.directory}")
    private String sftpInputDirectory;

    @Autowired
    private FileReaderFactory fileReaderFactory;

    @Bean
    public ItemReader<String[]> fileReader() {
        return new MultiFileReader(sftpService, fileReaderFactory, sftpInputDirectory);
    }

    @Bean
    public ItemWriter<Customer> customerWriter() {
        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
