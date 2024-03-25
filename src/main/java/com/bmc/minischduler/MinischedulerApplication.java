package com.bmc.minischduler;

import com.bmc.minischduler.service.TaskSchedulerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@SpringBootApplication
public class BmcMinischedulerApplication implements CommandLineRunner {

    @Autowired
    TaskSchedulerService bmcService;

    public static void main(String[] args) {
        SpringApplication.run(BmcMinischedulerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Init failed. Shutting down... Exception: ", e);
            throw e;
        }
    }

    private void init() throws Exception {
        bmcService.registerTasks();
    }
}

