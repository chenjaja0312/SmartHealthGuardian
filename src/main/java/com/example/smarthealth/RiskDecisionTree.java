package com.example.smarthealth;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskDecisionTree {
    public RiskResult classify(double sleepHours, int steps, int moodScore) {
        List<String> path = new ArrayList<>();

        // Decision Tree Layer 1: sleep hours
        if (sleepHours < 6.0) {
            path.add("Layer 1 Sleep: sleep < 6.0 hours → tired branch");

            // Decision Tree Layer 2: steps
            if (steps < 4000) {
                path.add("Layer 2 Steps: steps < 4000 → low activity branch");

                // Decision Tree Layer 3: mood score
                if (moodScore <= 4) {
                    path.add("Layer 3 Mood: mood <= 4 → HIGH risk");
                    return new RiskResult("HIGH", "High risk: sleep, activity, and mood are all low. Try to sleep earlier, take a short walk, and give yourself a gentle reset today.", path);
                }
                path.add("Layer 3 Mood: mood > 4 → MEDIUM risk");
                return new RiskResult("MEDIUM", "Medium risk: your sleep and activity are low, but your mood is not too bad. A small walk and earlier bedtime can help.", path);
            }

            path.add("Layer 2 Steps: steps >= 4000 → some activity branch");
            if (moodScore <= 4) {
                path.add("Layer 3 Mood: mood <= 4 → MEDIUM risk");
                return new RiskResult("MEDIUM", "Medium risk: you moved a bit, but sleep and mood need attention. Plan a lighter schedule tonight.", path);
            }
            path.add("Layer 3 Mood: mood > 4 → MEDIUM risk");
            return new RiskResult("MEDIUM", "Medium risk: sleep is still low. Keep your activity and mood stable, and prioritize rest.", path);
        }

        path.add("Layer 1 Sleep: sleep >= 6.0 hours → enough sleep branch");

        // Decision Tree Layer 2: steps
        if (steps < 4000) {
            path.add("Layer 2 Steps: steps < 4000 → low activity branch");
            if (moodScore <= 4) {
                path.add("Layer 3 Mood: mood <= 4 → MEDIUM risk");
                return new RiskResult("MEDIUM", "Medium risk: sleep is okay, but low activity and mood suggest you may need a small routine adjustment.", path);
            }
            path.add("Layer 3 Mood: mood > 4 → LOW risk");
            return new RiskResult("LOW", "Low risk: your sleep and mood are acceptable. Add a little movement if possible.", path);
        }

        path.add("Layer 2 Steps: steps >= 4000 → active branch");
        if (moodScore <= 4) {
            path.add("Layer 3 Mood: mood <= 4 → MEDIUM risk");
            return new RiskResult("MEDIUM", "Medium risk: your sleep and activity are fine, but mood is low. Take a break and check in with yourself.", path);
        }

        path.add("Layer 3 Mood: mood > 4 → LOW risk");
        return new RiskResult("LOW", "Low risk: great job! Your sleep, movement, and mood are in a healthy range today.", path);
    }
}
