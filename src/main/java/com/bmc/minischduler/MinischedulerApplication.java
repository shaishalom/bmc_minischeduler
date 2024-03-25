package com.bmc.minischduler;

import com.bmc.minischduler.service.TaskSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class MinischedulerApplication implements CommandLineRunner {

    @Autowired
    TaskSchedulerService taskSchedulerService;

    public static void main(String[] args) {
        SpringApplication.run(MinischedulerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            taskSchedulerService.init();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Init failed. Shutting down... Exception: ", e);
            throw e;
        }
    }


}

