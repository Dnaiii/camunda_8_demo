package com.kaributech.camunda.model;

import com.kaributech.camunda.config.RiskService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CarInsuranceWorker {

    @Autowired
    private RiskService riskService;

    @Autowired
    private ZeebeClient zeebeClient;

    @JobWorker(type = "")
    public void handleJob(ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        String riskCode = riskService.evaluateRisk(variables);

        zeebeClient.newCompleteCommand(job.getKey())
                .variables(Map.of("riskCode", riskCode))
                .send()
                .join();
    }
}