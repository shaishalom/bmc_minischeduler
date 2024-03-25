package com.bmc.minischduler.service;

import com.bmc.minischduler.job.JobFactory;
import com.bmc.minischduler.job.TaskJob;
import com.bmc.minischduler.model.Catalog;
import com.bmc.minischduler.model.SchedulerTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return
     */
    private boolean isTaskReady(SchedulerTask task) {
        //TODO handle in case time is empty and based on other task
        if (task.basedOn().isEmpty() && task.time().isEmpty()) {
            return false;
        }
        //if task already handled
        if (completedTasksMap.getOrDefault(task.id(),false)) {
            return false;
        }

        LocalTime taskTime = LocalTime.parse(task.time());
        //if current time is after time task, and based on is empty
        if (LocalTime.now().isAfter(LocalTime.now().withHour(taskTime.getHour()).withMinute(taskTime.getMinute())) &&
                (task.basedOn().isEmpty() )) {
            return true;
            //if based on is not empty , and not done in completed task
        } else if (!task.basedOn().isEmpty() && !completedTasksMap.getOrDefault(task.basedOn(),false)) {
            log.info("Task " + task.id() + " is waiting for Task " + task.basedOn() + " to be done");
            return false;
        }
        return false;
    }

//TODO
    //@Scheduled(fixedRate = 10000) //every midnight clean completedTasksMap
    @Scheduled(cron = "0 0 0 0 0 0") // Schedule a check every 10 seconds (adjust as needed)
    public void cleanMap () throws Exception {
        completedTasksMap.clear();
    }

    @Scheduled(fixedRate = 10000) // Schedule a check every 10 seconds (adjust as needed)
    public void runTasks() throws Exception {
        List<SchedulerTask> tasks = catalog.schedulerTask();


        for (SchedulerTask task : tasks) {
            // Check if task is ready (consider time, prerequisites, etc.)
            if (isTaskReady(task)) {
                runTask(task);
            }
        }
    }



    /**
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
