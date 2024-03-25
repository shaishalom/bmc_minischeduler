package com.bmc.minischduler.job;

import com.bmc.minischduler.model.SchedulerTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@AllArgsConstructor
public class RestJob implements TaskJob {

    SchedulerTask schduleTaskDTO;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("{} started", schduleTaskDTO.name());
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(schduleTaskDTO.details()))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("{} Ended.exit response: {}", schduleTaskDTO.name(),response);
        }catch(Exception e){
        }
    }
}
