package com.example.smarthealth;

import java.util.List;

public class RiskResult {
    private final String riskLevel;
    private final String message;
    private final List<String> decisionPath;

    public RiskResult(String riskLevel, String message, List<String> decisionPath) {
        this.riskLevel = riskLevel;
        this.message = message;
        this.decisionPath = decisionPath;
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
}
