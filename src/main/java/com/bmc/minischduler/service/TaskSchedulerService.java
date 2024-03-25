package com.bmc.minischduler.service;

import com.bmc.minischduler.constant.CommandType;
import com.bmc.minischduler.model.Catalog;
import com.bmc.minischduler.model.SchedulerTask;
import com.bmc.minischduler.service.job.BMCJob;
import com.bmc.minischduler.service.job.JobFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.quartz.Scheduler;

@Component
@Slf4j
public class BMCService {

    @Autowired
    Scheduler scheduler;

    @Autowired
    XMLReaderService xmlReaderService;

//    @Bean
//    @Scheduled(initialDelay = 1000) // Schedule a one-time load after 1 second
//    public void loadTasks() throws FileNotFoundException {
//        // Replace with your actual implementation (e.g., XmlReader or JsonReader)
//
//        List<SchedulerTask> countries = xmlMapper.readValue(is, new TypeReference<List<SchedulerTask>>() {
//        });
//    }



    private boolean isTaskReady(SchedulerTask task, Map<Integer, Boolean> completedTasks) {
        if (task.time().isEmpty()) {
            return false;
        }
        LocalTime taskTime = LocalTime.parse(task.time());
        if (LocalTime.now().isAfter(LocalTime.now().withHour(taskTime.getHour()).withMinute(taskTime.getMinute())) &&
                (task.basedOn().isEmpty() || completedTasks.getOrDefault(task.basedOn(), true))) {
            return true;
        } else if (!task.basedOn().isEmpty() && !completedTasks.get(task.basedOn())) {
            System.out.println("Task " + task.id() + " is waiting for Task " + task.basedOn() + " to be done");
            return false;
        }
        return false;
    }

    public void registerTasks() throws IOException {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Map<Integer, Boolean> completedTasks = new HashMap<>();
        Catalog catalog = null;
        try{
            catalog = xmlReaderService.readXmlFile();
        }catch(Exception e){
            catalog = new Catalog();
            SchedulerTask schedulerTask1 = new SchedulerTask("1","Print Command", CommandType.COMMAND,"echo task 1","18:30","");
            SchedulerTask schedulerTask2 = new SchedulerTask("2","Print Command", CommandType.COMMAND,"echo task 2","10:00","");
            catalog.schedulerTask.add(schedulerTask1);
            catalog.schedulerTask.add(schedulerTask2);
        }

        BMCJob job = null;
        for (SchedulerTask schduleTaskDTO: catalog.schedulerTask){

            if (isTaskReady(schduleTaskDTO, completedTasks)) {
                job = JobFactory.createJob(schduleTaskDTO);
                try {
                    job.execute(null);
                }catch(Exception e){
                    log.error("exception occured",e);
                }
            }
            JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                    .withIdentity(schduleTaskDTO.id(), schduleTaskDTO.name())
                    .build();

            //LocalDateTime localtime = LocalTime.parse(schduleTaskDTO.time());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(schduleTaskDTO.id())
                    .startAt(localtime.)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInHours(24)
                            .repeatForever()
                    )
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

//
//            schduleTaskDTO
        }
    }

    public void runTasks(){

    }

}
