package com.bmc.minischduler.service;

import com.bmc.minischduler.model.Catalog;
import com.bmc.minischduler.model.SchedulerTask;
import com.bmc.minischduler.job.TaskJob;
import com.bmc.minischduler.job.JobFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;

//TODO handle return code fro jobs
//update completedTasks after finish

@Component
@Slf4j
public class TaskSchedulerService {

    @Autowired
    Scheduler scheduler;

    @Autowired
    XMLReaderService xmlReaderService;

    @Autowired
    JSONReaderService jsonReaderService;

    Catalog catalog = null;

    Map<Integer, Boolean> completedTasksMap = new HashMap<>();


    /**
     * check if task did not run today
     * @param task
     * @param completedTasks
     * @return
     */
    private boolean isTaskReady(SchedulerTask task, Map<Integer, Boolean> completedTasks) {
        //TODO handle in case time is empty and based on other task
        if (task.basedOn().isEmpty() && task.time().isEmpty()) {
            return false;
        }
        LocalTime taskTime = LocalTime.parse(task.time());
        if (LocalTime.now().isAfter(LocalTime.now().withHour(taskTime.getHour()).withMinute(taskTime.getMinute())) &&
                (task.basedOn().isEmpty() ||  completedTasks.getOrDefault(task.basedOn(), true))) {
            return true;
        } else if (!task.basedOn().isEmpty() && !completedTasks.getOrDefault(task.basedOn(),false)) {
            System.out.println("Task " + task.id() + " is waiting for Task " + task.basedOn() + " to be done");
            return false;
        }
        return false;
    }

//TODO
    //@Scheduled(fixedRate = 10000) //everymidnoght clean completedTasksMap

    @Scheduled(fixedRate = 10000) // Schedule a check every 10 seconds (adjust as needed)
    public void runTasks() throws Exception {
        List<SchedulerTask> tasks = catalog.schedulerTask();


        for (SchedulerTask task : tasks) {
            // Check if task is ready (consider time, prerequisites, etc.)
            if (isTaskReady(task, completedTasksMap)) {
                runTask(task);
            }
        }
    }


    /**
     * do it every day
     * @throws IOException
     */
    public void init() throws IOException {
        try {
            catalog = xmlReaderService.readXmlFile();
            if (catalog == null) {
                catalog = jsonReaderService.readXmlFile();
            }
        }catch(Exception e){
            catalog = jsonReaderService.readXmlFile();
        }
        if (catalog!=null) {
            completedTasksMap = new HashMap<>();
        }
    }
    public void registerTasks() throws IOException {

        //load catalog

//            JobDetail jobDetail = JobBuilder.newJob(job.getClass())
//                    .withIdentity(schduleTaskDTO.id(), schduleTaskDTO.name())
//                    .build();


            //LocalDateTime localtime = LocalTime.parse(schduleTaskDTO.time());
//            Trigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity(schduleTaskDTO.id())
//                    .startAt(localtime.)
//                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                            .withIntervalInHours(24)
//                            .repeatForever()
//                    )
//                    .build();
//
//            scheduler.scheduleJob(jobDetail, trigger);

//
//            schduleTaskDTO
//        }
    }

    public void runTask(SchedulerTask schedulerTask){

        TaskJob job = JobFactory.createJob(schedulerTask);
        try {
            job.execute(null);
            //todo fill completedTask Map
        }catch(Exception e){
            log.error("exception occured",e);
        }

    }

}
