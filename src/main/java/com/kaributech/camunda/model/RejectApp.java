package com.kaributech.camunda.model;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RejectApp {

    @Autowired
    private ZeebeClient zeebeClient;

    @JobWorker(type = "reject")
    public void handleJob(ActivatedJob job) {
        try {
            System.out.println("Application rejected for variables: " + job.getVariablesAsMap());

            // Complete the job
            zeebeClient.newCompleteCommand(job.getKey())
                    .send()
                    .join();
        } catch (Exception e) {
            System.err.println("Error handling job: " + e.getMessage());
            // Optionally, fail the job with a message
            zeebeClient.newFailCommand(job.getKey())
                    .retries(0)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

}
