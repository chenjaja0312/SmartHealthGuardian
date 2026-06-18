package com.example.smarthealth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class HealthLogController {
    private final HealthLogRepository repository;
    private final RiskDecisionTree decisionTree;
    private final InformationGainService informationGainService;

    public HealthLogController(HealthLogRepository repository, RiskDecisionTree decisionTree, InformationGainService informationGainService) {
        this.repository = repository;
        this.decisionTree = decisionTree;
        this.informationGainService = informationGainService;
    }

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "app", "Smart Health Guardian");
    }

    @GetMapping("/health-logs")
    public List<HealthLog> getAllLogs() {
        return repository.findAll();
    }

    @PostMapping("/health-logs")
    public ResponseEntity<?> createLog(@RequestBody HealthLog log) {
        String error = validate(log);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        RiskResult result = decisionTree.classify(log.getSleepHours(), log.getSteps(), log.getMoodScore());
        log.setRiskLevel(result.getRiskLevel());
        HealthLog saved = repository.save(log);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/health-logs/{id}")
    public ResponseEntity<?> updateLog(@PathVariable int id, @RequestBody HealthLog log) {
        String error = validate(log);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        RiskResult result = decisionTree.classify(log.getSleepHours(), log.getSteps(), log.getMoodScore());
        log.setRiskLevel(result.getRiskLevel());
        boolean updated = repository.update(id, log);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Health log not found"));
        }
        return ResponseEntity.ok(repository.findById(id).orElse(log));
    }

    @DeleteMapping("/health-logs/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable int id) {
        boolean deleted = repository.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Health log not found"));
        }
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }


    @GetMapping("/health-logs/information-gain")
    public InformationGainResponse getInformationGainAnalysis() {
        return informationGainService.analyze();
    }

    @GetMapping("/health-logs/risk")
    public ResponseEntity<?> getLatestRisk() {
        var latest = repository.findLatest();
        if (latest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No health logs found"));
        }
        HealthLog log = latest.get();
        RiskResult result = decisionTree.classify(log.getSleepHours(), log.getSteps(), log.getMoodScore());
        return ResponseEntity.ok(new RiskResponse(result.getRiskLevel(), result.getMessage(), result.getDecisionPath(), log));
    }

    private String validate(HealthLog log) {
        if (log.getLogDate() == null || log.getLogDate().isBlank()) {
            return "logDate is required. Use YYYY-MM-DD.";
        }
        try {
            LocalDate.parse(log.getLogDate());
        } catch (DateTimeParseException ex) {
            return "logDate format must be YYYY-MM-DD.";
        }
        if (log.getSleepHours() < 0 || log.getSleepHours() > 24) {
            return "sleepHours must be between 0 and 24.";
        }
        if (log.getSteps() < 0) {
            return "steps must be 0 or greater.";
        }
        if (log.getMoodScore() < 1 || log.getMoodScore() > 10) {
            return "moodScore must be between 1 and 10.";
        }
        return null;
    }
}
