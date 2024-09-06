package com.kaributech.camunda.controller;


import com.kaributech.camunda.model.InsuranceRequest;
import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance")

public class CarController {


    @Autowired
    private ZeebeClient zeebeClient;

    @PostMapping("/start")
    public String startProcess(@RequestBody InsuranceRequest request) {
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("car_insure_1")
                .latestVersion()
                .variables(request)
                .send()
                .join();

        return "Process instance started";
    }
}
