package com.bmc.minischduler.service.job;

import com.bmc.minischduler.model.SchedulerTask;

public class JobFactory {

    public static TaskJob createJob(SchedulerTask schedulerTask) {
        switch (schedulerTask.type()) {
            case COMMAND:
                return new CommandJob(schedulerTask);
            case SCRIPT:
                return new ScriptJob(schedulerTask);
            case REST:
                return new RestJob(schedulerTask);
            default:
                return null;
        }
    }
}
