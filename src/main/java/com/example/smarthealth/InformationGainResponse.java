package com.example.smarthealth;

import java.util.List;

public class InformationGainResponse {
    private int totalRecords;
    private double baseEntropy;
    private String bestRootFeature;
    private List<FeatureGain> featureGains;
    private String conclusion;

    public InformationGainResponse() {
    }

    public InformationGainResponse(int totalRecords, double baseEntropy, String bestRootFeature, List<FeatureGain> featureGains, String conclusion) {
        this.totalRecords = totalRecords;
        this.baseEntropy = baseEntropy;
        this.bestRootFeature = bestRootFeature;
        this.featureGains = featureGains;
        this.conclusion = conclusion;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public double getBaseEntropy() {
        return baseEntropy;
    }

    public void setBaseEntropy(double baseEntropy) {
        this.baseEntropy = baseEntropy;
    }

    public String getBestRootFeature() {
        return bestRootFeature;
    }

    public void setBestRootFeature(String bestRootFeature) {
        this.bestRootFeature = bestRootFeature;
    }

    public List<FeatureGain> getFeatureGains() {
        return featureGains;
    }

    public void setFeatureGains(List<FeatureGain> featureGains) {
        this.featureGains = featureGains;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
}
