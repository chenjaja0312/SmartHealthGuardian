package com.example.smarthealth;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

@Service
public class InformationGainService {
    private final HealthLogRepository repository;

    public InformationGainService(HealthLogRepository repository) {
        this.repository = repository;
    }

    public InformationGainResponse analyze() {
        List<HealthLog> logs = repository.findAll();
        if (logs.isEmpty()) {
            return new InformationGainResponse(0, 0.0, "No data", List.of(), "No health logs are available for information gain analysis.");
        }

        double baseEntropy = entropy(logs);
        List<FeatureGain> gains = new ArrayList<>();

        gains.add(bestGainForFeature(
                logs,
                baseEntropy,
                "sleep_hours",
                HealthLog::getSleepHours,
                new double[]{5.0, 5.5, 6.0, 6.5, 7.0},
                "Tests which sleep-hour threshold best separates LOW / MEDIUM / HIGH risk records."
        ));

        gains.add(bestGainForFeature(
                logs,
                baseEntropy,
                "steps",
                log -> log.getSteps(),
                new double[]{3000, 3500, 4000, 5000, 6000},
                "Tests which step-count threshold best separates LOW / MEDIUM / HIGH risk records."
        ));

        gains.add(bestGainForFeature(
                logs,
                baseEntropy,
                "mood_score",
                log -> log.getMoodScore(),
                new double[]{3, 4, 5, 6},
                "Tests which mood-score threshold best separates LOW / MEDIUM / HIGH risk records."
        ));

        gains.sort(Comparator.comparingDouble(FeatureGain::getInformationGain).reversed());
        FeatureGain best = gains.get(0);
        String conclusion = String.format(
                "Best first split: %s at threshold %.1f with information gain %.4f. This shows the decision tree was checked using actual seed data instead of only guessed by rules.",
                best.getFeatureName(), best.getBestThreshold(), best.getInformationGain()
        );

        return new InformationGainResponse(logs.size(), round4(baseEntropy), best.getFeatureName(), gains, conclusion);
    }

    private FeatureGain bestGainForFeature(List<HealthLog> logs, double baseEntropy, String featureName,
                                           ToDoubleFunction<HealthLog> valueExtractor, double[] thresholds, String explanation) {
        double bestThreshold = thresholds[0];
        double bestGain = Double.NEGATIVE_INFINITY;

        for (double threshold : thresholds) {
            List<HealthLog> left = new ArrayList<>();
            List<HealthLog> right = new ArrayList<>();

            for (HealthLog log : logs) {
                if (valueExtractor.applyAsDouble(log) < threshold) {
                    left.add(log);
                } else {
                    right.add(log);
                }
            }

            double weightedEntropy = ((double) left.size() / logs.size()) * entropy(left)
                    + ((double) right.size() / logs.size()) * entropy(right);
            double gain = baseEntropy - weightedEntropy;

            if (gain > bestGain) {
                bestGain = gain;
                bestThreshold = threshold;
            }
        }

        return new FeatureGain(featureName, bestThreshold, round4(bestGain), explanation);
    }

    private double entropy(List<HealthLog> logs) {
        if (logs.isEmpty()) {
            return 0.0;
        }

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (HealthLog log : logs) {
            counts.merge(log.getRiskLevel(), 1, Integer::sum);
        }

        double entropy = 0.0;
        for (int count : counts.values()) {
            double probability = (double) count / logs.size();
            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }

    private double round4(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
