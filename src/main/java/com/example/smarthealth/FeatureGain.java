package com.example.smarthealth;

public class FeatureGain {
    private String featureName;
    private double bestThreshold;
    private double informationGain;
    private String explanation;

    public FeatureGain() {
    }

    public FeatureGain(String featureName, double bestThreshold, double informationGain, String explanation) {
        this.featureName = featureName;
        this.bestThreshold = bestThreshold;
        this.informationGain = informationGain;
        this.explanation = explanation;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public double getBestThreshold() {
        return bestThreshold;
    }

    public void setBestThreshold(double bestThreshold) {
        this.bestThreshold = bestThreshold;
    }

    public double getInformationGain() {
        return informationGain;
    }

    public void setInformationGain(double informationGain) {
        this.informationGain = informationGain;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
