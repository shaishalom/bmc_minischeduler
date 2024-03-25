package com.bmc.minischduler.service.job;

import com.bmc.minischduler.model.SchedulerTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ScriptJob implements TaskJob {

    SchedulerTask schduleTaskDTO;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {

            log.info("{} started", schduleTaskDTO.name());
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            ProcessBuilder processBuilder = new ProcessBuilder();

            if (isWindows) {
                processBuilder.command("cmd.exe", "/c", "start", "cmd.exe", "/c", schduleTaskDTO.details());
            } else {
                processBuilder.command("sh", "-c", "start", "sh", "-c", schduleTaskDTO.details());
            }
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT); // Inherit command output
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT); // Inherit command errors
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            log.info("{} Ended.exit code: {}", schduleTaskDTO.name(),exitCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }
}
