package com.projectX.projectX.domain.cafe.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CafeBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveCafe(List<Map<String, String>> cafes) {
        String sql =
            "INSERT INTO cafe (name, cafe_type, address, latitude, longitude, created_at, cafe_id)"
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                    ps.setString(7, cafes.get(i).get("cafeId"));

                }

                @Override
                public int getBatchSize() {
                    return cafes.size();
                }
            }
        );

    }

    @Transactional
    public void updateCafe(List<Map<String, String>> cafes) {
        String sql =
            "UPDATE cafe SET name = ?, cafe_type = ?, address = ?, latitude = ?, longitude = ?, updated_at = ?, cafe_id = ? "
                +
                "WHERE id = ?";

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
                    ps.setString(7, cafes.get(i).get("cafeId"));
                    ps.setLong(8, Long.parseLong(
                        cafes.get(i).get("id"))); // assuming 'id' is provided for each cafe
                }

                @Override
                public int getBatchSize() {
                    return cafes.size();
                }
            }
        );
    }
}
