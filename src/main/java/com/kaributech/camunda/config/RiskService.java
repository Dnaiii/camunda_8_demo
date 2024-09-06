package com.kaributech.camunda.config;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Map;

@Service
public class RiskService {

    private final DmnEngine dmnEngine;
    private final DmnDecision decision;

    public RiskService() {
        dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();

        // Load the DMN file from the classpath
        InputStream dmnFileStream = getClass().getResourceAsStream("/static/Request_Car_Insurance.dmn");
        if (dmnFileStream == null) {
            throw new IllegalArgumentException("DMN file not found");
        }

        decision = dmnEngine.parseDecision("Decision_0x1or3u", dmnFileStream);
    }

    public String evaluateRisk(Map<String, Object> variables) {
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, variables);
        return result.getSingleResult().getSingleEntry();
    }
}
