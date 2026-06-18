package com.example.smarthealth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;
    private final HealthLogRepository repository;
    private final RiskDecisionTree decisionTree;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate, HealthLogRepository repository, RiskDecisionTree decisionTree) {
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
        this.decisionTree = decisionTree;
    }

    @Override
    public void run(String... args) throws Exception {
        Files.createDirectories(Path.of("data"));
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS health_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    log_date DATE NOT NULL,
                    sleep_hours REAL NOT NULL,
                    steps INTEGER NOT NULL,
                    mood_score INTEGER NOT NULL,
                    risk_level TEXT
                )
                """);

        if (repository.count() == 0) {
            seedNinetyDays();
        }
    }

    private void seedNinetyDays() {
        LocalDate start = LocalDate.now().minusDays(89);

        // 25 clear high-risk examples: low sleep, low steps, low mood.
        for (int i = 0; i < 25; i++) {
            double sleep = 4.1 + (i % 8) * 0.18;
            int steps = 1100 + (i % 9) * 240;
            int mood = 1 + (i % 4);
            insert(start.plusDays(i), sleep, steps, mood);
        }

        // 40 mixed medium examples: enough variety for middle decision-tree branches.
        for (int i = 25; i < 65; i++) {
            int j = i - 25;
            double sleep = 5.6 + (j % 10) * 0.16;
            int steps = 3300 + (j % 12) * 270;
            int mood = 4 + (j % 3);
            if (j % 5 == 0) {
                sleep = 6.6;
                steps = 2800;
                mood = 3;
            }
            insert(start.plusDays(i), sleep, steps, mood);
        }

        // 25 clear low-risk examples: enough sleep, many steps, good mood.
        for (int i = 65; i < 90; i++) {
            int j = i - 65;
            double sleep = 7.0 + (j % 9) * 0.22;
            int steps = 6200 + (j % 10) * 410;
            int mood = 6 + (j % 4);
            insert(start.plusDays(i), sleep, steps, mood);
        }
    }

    private void insert(LocalDate date, double sleep, int steps, int mood) {
        double roundedSleep = Double.parseDouble(String.format(Locale.US, "%.1f", sleep));
        String risk = decisionTree.classify(roundedSleep, steps, mood).getRiskLevel();
        repository.insertSeed(date.toString(), roundedSleep, steps, mood, risk);
    }
}
