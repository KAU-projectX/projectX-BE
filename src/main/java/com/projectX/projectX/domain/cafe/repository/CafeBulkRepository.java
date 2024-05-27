package com.projectX.projectX.domain.cafe.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CafeBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Map<String, String>> cafes) {
        String sql =
            "INSERT INTO cafe (name, cafe_type, address, latitude, longitude, created_at)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, cafes.get(i).get("name"));
                    ps.setString(2, cafes.get(i).get("cafeType"));
                    ps.setString(3, cafes.get(i).get("address"));
                    ps.setDouble(4, Double.parseDouble(cafes.get(i).get("latitude")));
                    ps.setDouble(5, Double.parseDouble(cafes.get(i).get("longitude")));
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                }

                @Override
                public int getBatchSize() {
                    return cafes.size();
                }
            }
        );

    }
}
