package com.example.smarthealth;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class HealthLogRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<HealthLog> rowMapper = (rs, rowNum) -> new HealthLog(
            rs.getInt("id"),
            rs.getString("log_date"),
            rs.getDouble("sleep_hours"),
            rs.getInt("steps"),
            rs.getInt("mood_score"),
            rs.getString("risk_level")
    );

    public HealthLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<HealthLog> findAll() {
        return jdbcTemplate.query("SELECT * FROM health_logs ORDER BY log_date DESC, id DESC", rowMapper);
    }

    public Optional<HealthLog> findById(int id) {
        List<HealthLog> logs = jdbcTemplate.query("SELECT * FROM health_logs WHERE id = ?", rowMapper, id);
        return logs.stream().findFirst();
    }

    public Optional<HealthLog> findLatest() {
        List<HealthLog> logs = jdbcTemplate.query("SELECT * FROM health_logs ORDER BY log_date DESC, id DESC LIMIT 1", rowMapper);
        return logs.stream().findFirst();
    }

    public HealthLog save(HealthLog log) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO health_logs (log_date, sleep_hours, steps, mood_score, risk_level) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, log.getLogDate());
            ps.setDouble(2, log.getSleepHours());
            ps.setInt(3, log.getSteps());
            ps.setInt(4, log.getMoodScore());
            ps.setString(5, log.getRiskLevel());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            log.setId(key.intValue());
        }
        return log;
    }

    public boolean update(int id, HealthLog log) {
        int rows = jdbcTemplate.update(
                "UPDATE health_logs SET log_date = ?, sleep_hours = ?, steps = ?, mood_score = ?, risk_level = ? WHERE id = ?",
                log.getLogDate(), log.getSleepHours(), log.getSteps(), log.getMoodScore(), log.getRiskLevel(), id
        );
        return rows > 0;
    }

    public boolean delete(int id) {
        int rows = jdbcTemplate.update("DELETE FROM health_logs WHERE id = ?", id);
        return rows > 0;
    }

    public int count() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM health_logs", Integer.class);
        return count == null ? 0 : count;
    }

    public void insertSeed(String date, double sleepHours, int steps, int moodScore, String riskLevel) {
        jdbcTemplate.update(
                "INSERT INTO health_logs (log_date, sleep_hours, steps, mood_score, risk_level) VALUES (?, ?, ?, ?, ?)",
                date, sleepHours, steps, moodScore, riskLevel
        );
    }
}
