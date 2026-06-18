package com.example.smarthealth;

import java.util.List;

public class RiskResponse {
    private String riskLevel;
    private String message;
    private List<String> decisionPath;
    private HealthLog latestLog;

    public RiskResponse(String riskLevel, String message, List<String> decisionPath, HealthLog latestLog) {
        this.riskLevel = riskLevel;
        this.message = message;
        this.decisionPath = decisionPath;
        this.latestLog = latestLog;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDecisionPath() {
        return decisionPath;
    }

    public HealthLog getLatestLog() {
        return latestLog;
    }
}
